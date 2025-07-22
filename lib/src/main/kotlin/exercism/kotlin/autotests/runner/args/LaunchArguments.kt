package exercism.kotlin.autotests.runner.args

import exercism.kotlin.autotests.runner.BuildConfig
import utils.resolveAsDir
import java.io.File

/**
 * Data structure to keep resolved launch arguments
 */
data class LaunchArguments(
    /**
     * Exercise slug (e.g. `hello-world`). Currently ignored.
     */
    val exerciseSlug: String,
    /**
     * [File] pointing to solutions directory.
     */
    val solutionsDir: File,
    /**
     * [File] pointing to resolved `results.json` file in output directory.
     */
    val resultFile: File
) {

    companion object
}

/**
 * Parse launch arguments and validate them. [IllegalStateException] can be thrown if some arguments are invalid or not provided.
 *
 * Typically used in `main()`:
 * ```kotlin
 * fun main(args: Array<String>) {
 *   val parsedArguments = LaunchArguments.parseAndValidate(args)
 *   ...
 * }
 * ```
 */
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
