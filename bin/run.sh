#!/bin/bash

export JAVA_TOOL_OPTIONS="-Xss128m -Xms256m -Xmx2G -XX:+UseG1GC"
cp /root/pom.xml $2

# Temporarily un-ignore tests
find $2/src/test/kotlin -type f -name '*Test.kt' -execdir cp {} {}.bak \;
find $2/src/test/kotlin -type f -name '*Test.kt' -exec sed -i "s/@Ignore(.*)//g;s/@Ignore//g;" {} \;

# See Dockerfile for details
java -jar /opt/test-runner/autotest-runner.jar $1 $2 $3

# Revert the un-ignore changes
find $2/src/test/kotlin -type f -name '*Test.kt' -execdir mv {}.bak {} \;

rm -f $2/pom.xml
