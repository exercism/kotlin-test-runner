#!/bin/sh

# See Dockerfile for details
# cp -R $2 /tmp
# cd /tmp/solution
# /opt/kotlinc/bin/kotlinc-jvm src/main/kotlin/HelloWorld.kt -include-runtime -d hello.jar
# java -jar hello.jar



# TODO: check if kotlinc-jvm can have output dir

cp -R $2 /tmp
cd /tmp/solution

/opt/kotlinc/bin/kotlinc-jvm \
    src/main/kotlin/HelloWorld.kt \
    src/test/kotlin/HelloWorldTest.kt \
    -include-runtime \
    -d hello.jar \
    -cp '/home/.gradle/./caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar:/home/.gradle/./caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-test-junit/1.3.60/1e97731f2a360b2b6f5295a27c7e1004fc131f7e/kotlin-test-junit-1.3.60.jar'

# java -jar /opt/test-runner/autotest-runner.jar $1 $2 $3
