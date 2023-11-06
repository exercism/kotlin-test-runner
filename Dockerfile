# === Build builder image ===

FROM gradle:8.4.0-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/ ./

# Build test runner
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar .

FROM maven:3.8.6-jdk-11-slim AS cache

# Ensure exercise dependencies are downloaded
WORKDIR /opt/exercise
COPY examples/full .
COPY examples/template/pom.xml .
RUN mvn test dependency:go-offline -DexcludeReactor=false

# === Build runtime image ===

FROM maven:3.8.6-jdk-11-slim
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ bin/
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=cache /root/.m2 /root/.m2

# Copy Maven pom.xml
COPY --from=cache /opt/exercise/pom.xml /root/pom.xml

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
