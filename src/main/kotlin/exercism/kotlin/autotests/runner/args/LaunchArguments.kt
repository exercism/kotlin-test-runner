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

fun LaunchArguments.Companion.parseAndValidateFrom(arguments: Array<String>): LaunchArguments {
    check(arguments.size == 3) { "This test exercism.kotlin.autotests.runner requires exactly 3 arguments, but ${arguments.size} provided" }

    val outputDir = arguments[2].resolveAsDir()
    with(outputDir) {
        check(!exists() || isDirectory) { "Output directory '${absolutePath}' is not a directory (but file)" }
    }

    val args = LaunchArguments(
        exerciseSlug = arguments[0],
        solutionsDir = arguments[1].resolveAsDir(),
        resultFile = outputDir.resolve("results.json")
    )

    with(args) {
        check(exerciseSlug.isNotBlank()) { "Exercise slug should not be blank" }
        check(solutionsDir.exists()) { "Solutions directory '${solutionsDir.absolutePath}' does not exist" }

        if (!BuildConfig.overrideResultFile) {
            check(!resultFile.exists()) { "Result file '${resultFile.absolutePath}' exists" }
        }
    }

    println("Parsed arguments: $args")

    return args
}
