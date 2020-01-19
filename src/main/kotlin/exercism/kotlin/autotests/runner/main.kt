package exercism.kotlin.autotests.runner

import exercism.kotlin.autotests.executor.executeOnEnvironment
import exercism.kotlin.autotests.executor.executor
import exercism.kotlin.autotests.runner.args.LaunchArguments
import exercism.kotlin.autotests.runner.args.parseAndValidate
import exercism.kotlin.autotests.runner.report.exportReportToFile

/**
 * Kotlin auto-test runner tool.
 * It uses provided `gradlew` to execute tests, parses JUnit report and exports data to `results.json`.
 *
 * [Tool interface specification in exercism repo](https://github.com/exercism/automated-tests/blob/master/docs/interface.md)
 *
 * Usage:
 * ```bash
 * ./run.sh <exercise-slug> <path-to-solutions-directory> <path-to-output-directory>
 * ```
 *
 * Example: `./run.sh hello-world examples/1/ example/out
 *
 * After execution `results.json` file will be created in output folder.
 * Original content of solutions directory will not be changed
 * but build files will be created during compilation (can be cleared with `./gradlew clean`)
 */
fun main(arguments: Array<String>) {
    val args = LaunchArguments.parseAndValidate(arguments)
    val result = executeOnEnvironment(args, ::executor)

    result.exportReportToFile(args.resultFile)
}

object BuildConfig {
    /** Allow to override existing `results.json` file. */
    const val overrideResultFile = true

    /** Will not remove working (temporary) dir after finishing if set to **true**. */
    const val keepWorkingDir = true
}
