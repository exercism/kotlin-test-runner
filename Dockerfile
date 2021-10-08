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
COPY pre-compile .
RUN gradle build

# === Build runtime image ===

FROM gradle:6.8-jdk11

# Copy cached dependencies
COPY --from=build /home/gradle /home/gradle

# TODO: move this to build image and then copy
WORKDIR /opt
RUN curl -L -O https://github.com/JetBrains/kotlin/releases/download/v1.5.31/kotlin-compiler-1.5.31.zip
RUN unzip kotlin-compiler-1.5.31.zip
RUN ls -al kotlinc

WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/run.sh bin/run.sh
# COPY --from=build /home/builder/autotest-runner.jar ./

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
