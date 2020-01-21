[kotlin-test-runner](../index.md) / [exercism.kotlin.autotests.runner](index.md) / [main](./main.md)

# main

`fun main(arguments: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Kotlin auto-test runner tool.
It uses provided `gradlew` to execute tests, parses JUnit report and exports data to `results.json`.

[Tool interface specification in exercism repo](https://github.com/exercism/automated-tests/blob/master/docs/interface.md)

Usage:

``` bash
./run.sh <exercise-slug> <path-to-solutions-directory> <path-to-output-directory>
```

Example: `./run.sh hello-world examples/1/ example/out

After execution `results.json` file will be created in output folder.
Original content of solutions directory will not be changed
but build files will be created during compilation (can be cleared with `./gradlew clean`)

