package runner

import executor.RuntimeConfiguration
import executor.executeOnEnvironment
import executor.executor
import java.io.File

private val DEBUG = object {
    val overrideResultFile = true
}

fun main(arguments: Array<String>) {
    val args = parseAndValidate(arguments)

    val exercismRepo = File("../exercism-kotlin")

    val selectedExerciseDir = exercismRepo
        .resolve("exercises")
        .resolve(args.exerciseSlug)
    with(selectedExerciseDir) {
        check(exists()) { "Exercise directory '$absolutePath' not found" }
        check(isDirectory) { "Exercise directory '$absolutePath' is a file" }
    }

    val config = buildRuntimeConfig(
        exerciseDir = selectedExerciseDir,
        solutionsDir = args.solutionsDir,
        templateDir = exercismRepo.resolve("_template")
    )
    executeOnEnvironment(config, ::executor)
}

private fun buildRuntimeConfig(exerciseDir: File, solutionsDir: File, templateDir: File): RuntimeConfiguration {
    return object : RuntimeConfiguration {
        override val workingDirRoot = File("out")
        override val sourcesDir = solutionsDir.resolve("src/main/kotlin")
        override val testsDir = exerciseDir.resolve("src/test/kotlin")
        override val templateDir = templateDir

        // Debug
        override val purgeExistingWorkingDirBefore = true
        override val keepWorkingDirAfter = true
    }
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

private data class Args(
    val exerciseSlug: String,
    val solutionsDir: File,
    val resultFile: File
)

private fun String.resolveAsDir(): File = File(".").resolve(this)
