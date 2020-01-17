package runner

import java.io.File

private val DEBUG = object {
    val overrideResultFile = true
}

fun main(arguments: Array<String>) {
    val args = parseAndValidate(arguments)

    println(args)
}

private fun parseAndValidate(arguments: Array<String>): Args {
    check(arguments.size == 3) { "This test runner requires exactly 3 arguments, but ${arguments.size} provided" }

    val outputDir = arguments[2].resolveAsDir()
    check(!outputDir.exists() || outputDir.isDirectory) { "Output directory '${outputDir.absolutePath}' is not a directory (but file)" }

    val args = Args(
        exerciseSlug = arguments[0],
        solutionsDir = arguments[1].resolveAsDir(),
        resultFile = outputDir.resolve("results.json")
    )

    check(args.solutionsDir.exists()) { "Solutions directory '${args.solutionsDir.absolutePath}' does not exist" }

    if (!DEBUG.overrideResultFile) {
        check(!args.resultFile.exists()) { "Result file '${args.resultFile.absolutePath}' exists" }
    }

    return args
}

private data class Args(
    val exerciseSlug: String,
    val solutionsDir: File,
    val resultFile: File
)

private fun String.resolveAsDir(): File = File(".").resolve(this)
