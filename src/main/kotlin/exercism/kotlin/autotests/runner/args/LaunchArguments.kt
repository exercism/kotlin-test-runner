package exercism.kotlin.autotests.runner.args

import exercism.kotlin.autotests.runner.BuildConfig
import utils.resolveAsDir
import java.io.File

data class LaunchArguments(
    val exerciseSlug: String,
    val solutionsDir: File,
    val resultFile: File
) {

    companion object
}

fun LaunchArguments.Companion.parseAndValidate(arguments: Array<String>): LaunchArguments {
    check(arguments.size == 3) { "Expecting exactly 3 arguments, but ${arguments.size} provided" }

    val args = LaunchArguments(
        exerciseSlug = arguments[0],
        solutionsDir = arguments[1].resolveAsDir(),
        resultFile = arguments[2].resolveAsDir().resolve("results.json")
    )

    if (!BuildConfig.overrideResultFile) {
        check(!args.resultFile.exists()) { "Result file '${args.resultFile.absolutePath}' exists" }
    }

    println("Parsed arguments: $args")

    return args
}
