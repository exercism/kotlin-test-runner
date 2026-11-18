# === Build builder image ===

FROM docker.io/library/gradle:9.5.1-jdk21@sha256:4a011ed0edfe2feb7dace27d1fedf22caf217918b59932a95605cb7beec142c4 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/ ./

# Build test runner
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar .

FROM docker.io/library/maven:3.9.16-eclipse-temurin-21-alpine@sha256:c3b70520630a94abc4bb9d87bb3c6a0bb44f936e0ef1035233727411c7b5b854 AS cache

# Ensure exercise dependencies are downloaded
WORKDIR /opt/exercise
COPY examples/full .
COPY examples/template/pom.xml .
RUN mvn test dependency:go-offline -DexcludeReactor=false

# Record what the runner needs to compile and test without Maven:
# the classpaths Maven resolves for the exercise pom.xml, the Kotlin compiler's classpath,
# and a small JUnit Platform harness (see fast/FastTestHarness.java).
# The harness uses the same JUnit Platform launcher as Surefire, which matches the engine's version.
WORKDIR /opt/fast-build
COPY fast/ .
RUN set -e; \
    out=/opt/test-runner/fast; mkdir -p $out/harness; \
    kotlin_version=$(sed -n 's:.*<kotlin.version>\(.*\)</kotlin.version>.*:\1:p' /opt/exercise/pom.xml); \
    mvn -q -f /opt/exercise/pom.xml dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=$out/main.classpath; \
    mvn -q -f /opt/exercise/pom.xml dependency:build-classpath -Dmdep.outputFile=$out/test.classpath; \
    mvn -q dependency:build-classpath -Dkotlin.version=$kotlin_version -Dmdep.outputFile=$out/compiler.classpath; \
    platform_version=$(tr ':' '\n' < $out/test.classpath | sed -n 's:.*/junit-platform-engine-\(.*\)\.jar:\1:p'); \
    launcher=/root/.m2/repository/org/junit/platform/junit-platform-launcher/$platform_version/junit-platform-launcher-$platform_version.jar; \
    test -f $launcher; \
    javac -d $out/harness -cp "$launcher:$(cat $out/test.classpath)" FastTestHarness.java; \
    echo "$launcher:$out/harness" > $out/harness.classpath

# === Build runtime image ===

FROM docker.io/library/maven:3.9.16-eclipse-temurin-21-alpine@sha256:c3b70520630a94abc4bb9d87bb3c6a0bb44f936e0ef1035233727411c7b5b854
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ bin/
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=cache /root/.m2 /root/.m2
COPY --from=cache /opt/test-runner/fast fast/

# Copy Maven pom.xml
COPY --from=cache /opt/exercise/pom.xml /root/pom.xml

# Train the class data sharing archive by running the runner once on a sample exercise.
COPY examples/full /tmp/training
RUN JAVA_CDS=-XX:ArchiveClassesAtExit=/opt/test-runner/fast/runner.jsa \
        sh bin/run.sh hello-world /tmp/training /tmp/training \
    && grep -q '"status": "pass"' /tmp/training/results.json \
    && rm -rf /tmp/training /tmp/kotlin-autotest-runnner*

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
