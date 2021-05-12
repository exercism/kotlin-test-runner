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

# === Build runtime image ===

FROM gradle:6.8-jdk11
ARG WORKDIR="/opt/test-runner"

# Copy binary and launcher script
COPY bin/run.sh ${WORKDIR}/run.sh
COPY --from=build /home/builder/autotest-runner.jar ${WORKDIR}

# Cache Kotlin dependencies
#COPY cache-warmup/ /tmp/cache-warmup/
#RUN cd /tmp/cache-warmup/ \
#    && rm -r .gradle/ build/ \
#    && gradle --no-daemon test

WORKDIR $WORKDIR

ENTRYPOINT ["sh", "/opt/test-runner/run.sh"]
