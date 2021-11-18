#!/bin/sh

export JAVA_TOOL_OPTIONS="-Xss128m -Xms256m -Xmx2G -XX:+UseG1GC"
cp /root/pom.xml $2

# See Dockerfile for details
java -jar /opt/test-runner/autotest-runner.jar $1 $2 $3

rm -f $2/pom.xml
