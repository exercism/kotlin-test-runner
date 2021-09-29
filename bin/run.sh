#!/bin/sh

cd $2
gradle --status
gradle test -Dorg.gradle.daemon.idleTimeout=360000000
gradle --status
# cat ~/.gradle/daemon/6.8.3/daemon-41.out.log

# cat ~/.gradle/daemon/6.8.3/daemon-42.out.log
# cat ~/.gradle/daemon/6.8.3/daemon-40.out.log

# See Dockerfile for details
# java -jar /opt/test-runner/autotest-runner.jar $1 $2 $3
