package exercism.kotlin.autotests.runner

import exercism.kotlin.autotests.executor.executeOnEnvironment
import exercism.kotlin.autotests.executor.executor
import exercism.kotlin.autotests.runner.args.LaunchArguments
import exercism.kotlin.autotests.runner.args.parseAndValidateFrom
import exercism.kotlin.autotests.runner.report.exportReportToFile

fun main(arguments: Array<String>) {
    val args = LaunchArguments.parseAndValidateFrom(arguments)
    val result = executeOnEnvironment(args, ::executor)

    result.exportReportToFile(args.resultFile)
}

object BuildConfig {
    const val overrideResultFile = true

    const val keepWorkingDir = true
}
