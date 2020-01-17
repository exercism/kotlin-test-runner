package runner

import executor.ExecutionResult
import executor.executeOnEnvironment
import executor.executor
import java.io.File

private val DEBUG = object {
    val overrideResultFile = true
}

fun main(arguments: Array<String>) {
    val args = parseAndValidate(arguments)
    val config = buildRuntimeConfigFrom(args)

    val result = executeOnEnvironment(config, ::executor)
    result.exportTo(args.resultFile)
}

private fun parseAndValidate(arguments: Array<String>): Args {
    check(arguments.size == 3) { "This test runner requires exactly 3 arguments, but ${arguments.size} provided" }

    val outputDir = arguments[2].resolveAsDir()
    with(outputDir) {
        check(!exists() || isDirectory) { "Output directory '${absolutePath}' is not a directory (but file)" }
    }

    val args = Args(
        exerciseSlug = arguments[0],
        solutionsDir = arguments[1].resolveAsDir(),
        resultFile = outputDir.resolve("results.json")
    )

    with(args) {
        check(exerciseSlug.isNotBlank()) { "Exercise slug should not be blank" }
        check(solutionsDir.exists()) { "Solutions directory '${solutionsDir.absolutePath}' does not exist" }

        if (!DEBUG.overrideResultFile) {
            check(!resultFile.exists()) { "Result file '${resultFile.absolutePath}' exists" }
        }
    }

    println("Parsed arguments: $args")

    return args
}

fun ExecutionResult.exportTo(file: File) {
    val status = when (this) {
        ExecutionResult.Success -> "pass"
        ExecutionResult.Fail -> "fail"
        ExecutionResult.Error -> "error"
    }

    file.writeText("""
        |{
        |  "status": "$status",
        |  "message": null,
        |  "tests": []
        |}
    """.trimMargin())
}

data class Args(
    val exerciseSlug: String,
    val solutionsDir: File,
    val resultFile: File
)

private fun String.resolveAsDir(): File = File(".").resolve(this)
