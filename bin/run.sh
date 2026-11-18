#!/bin/bash

export JAVA_TOOL_OPTIONS="-Xss128m -Xms256m -Xmx2G -XX:+UseG1GC"
cp /root/pom.xml $2

# Temporarily un-ignore tests
find $2/src/test/kotlin -type f -name '*Test.kt' -execdir cp {} {}.bak \;
find $2/src/test/kotlin -type f -name '*Test.kt' -exec sed -i "s/@Ignore(.*)//g;s/@Ignore//g;" {} \; # for JUnit 4
find $2/src/test/kotlin -type f -name '*Test.kt' -exec sed -i "s/@Disabled(.*)//g;s/@Disabled//g;" {} \; # for JUnit 5

# See Dockerfile for details.
# The Kotlin compiler is on the classpath so the runner can compile without Maven.
# The class data sharing archive (trained at image build) holds the runner's and the compiler's
# classes already parsed, so the JVM does not load them from the jars on every run.
# The image build sets JAVA_CDS to create the archive instead.
java ${JAVA_CDS:--XX:SharedArchiveFile=/opt/test-runner/fast/runner.jsa} -Xlog:cds=off -Xlog:cds+dynamic=off \
    -cp "/opt/test-runner/autotest-runner.jar:$(cat /opt/test-runner/fast/compiler.classpath)" \
    exercism.kotlin.autotests.runner.MainKt $1 $2 $3

# Revert the un-ignore changes
find $2/src/test/kotlin -type f -name '*Test.kt' -execdir mv {}.bak {} \;

rm -f $2/pom.xml
