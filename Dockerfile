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

# === Build runtime image ===

FROM docker.io/library/maven:3.9.16-eclipse-temurin-21-alpine@sha256:c3b70520630a94abc4bb9d87bb3c6a0bb44f936e0ef1035233727411c7b5b854
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ bin/
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=cache /root/.m2 /root/.m2

# Copy Maven pom.xml
COPY --from=cache /opt/exercise/pom.xml /root/pom.xml

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
