package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.executor.ExecutionResult.Status
import utils.junit.parseJUnit4Results
import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.nio.file.Files

fun executor(env: Environment): ExecutionResult {
    // Copy project skeleton from references
    env.templateDir.copyRecursively(env.workingDir)
    env.workingDir.resolve("gradlew").setExecutable(true)

    // Copy sources from solution directory
    env.sourcesDir.copyRecursively(env.workingDir.resolve("src/main/kotlin"))

    // Copy tests removing
    val destinationTestsDir = env.workingDir.resolve("src/test/kotlin")
    env.testsDir.copyRecursively(destinationTestsDir)

    // Remove all `@Ignore` annotations in tests
    Files.walk(destinationTestsDir.toPath())
        .filter { it.endsWith("Test.kt") }
        .map { it.toFile() }
        .filter { it.readLines().any { line -> line.contains("@Ignore") } }
        .forEach { file ->
            val newContent = file
                .readLines()
                .filterNot { it.trim() == "@Ignore" }

            file.writeText(newContent.joinToString("\n"))
        }

    // Run tests
    val exitCode = executeTests(env.workingDir)

    // Prepare report
    return parseTestResults(env.workingDir, exitCode)
}

typealias ExitCode = Int

private fun executeTests(workingDir: File): ExitCode {
    println("Running gradle")

    val process = ProcessBuilder("./gradlew", "--no-daemon", "--continue", "test")
        .directory(workingDir)
        .redirectOutput(Redirect.INHERIT)
        .redirectError(Redirect.INHERIT)
        .start()

    val exitCode = process.waitFor()
    println("Gradle finished with exit code $exitCode")

    return exitCode
}

private fun parseTestResults(workingDir: File, exitCode: ExitCode): ExecutionResult {
    val testResultsDir = workingDir.resolve("build/test-results/test/")
    val resultsFile = testResultsDir.listFiles()!!
        .filter { it.isFile }
        .filter { it.extension == "xml" }
        .also { check(it.size <= 1) { "More than one test result files are found. Can't process it now" } }
        .firstOrNull()
        ?: return ExecutionResult(Status.Error)

    val suit = parseJUnit4Results(resultsFile)

    return ExecutionResult(
        status = (if (exitCode == 0) Status.Success else Status.Fail),
        suits = listOf(suit)
    )
}
