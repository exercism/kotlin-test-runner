# === Build builder image ===

FROM gradle:6.8-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY src ./src
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# Build test runner
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar .

# Ensure exercise dependencies are downloaded
WORKDIR /opt/exercise
COPY examples/full .
RUN gradle build

# === Build runtime image ===

FROM gradle:6.8-jdk11
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/run.sh bin/run.sh
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=build /home/gradle /home/gradle

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
