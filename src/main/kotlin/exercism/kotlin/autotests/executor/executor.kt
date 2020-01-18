package exercism.kotlin.autotests.executor

import utils.junit.parseJUnit4Results
import java.io.File
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
    val buildResult = executeBuild(env.workingDir)

    // Prepare report
    return buildExecutionResult(env.workingDir, buildResult)
}

data class BuildResult(
    val succeeded: Boolean,
    val logFile: File
)

val BuildResult.failed get() = !succeeded

private fun executeBuild(workingDir: File): BuildResult {
    println("Running gradle")

    val logFile = workingDir.resolve("__out.log")
    val exitCode = runGradleProcess(workingDir, logFile)

    println("Gradle finished with exit code $exitCode")
    println("=== Log START ===")
    println(logFile.readText())
    println("=== Log END ===")

    return BuildResult(
        succeeded = exitCode == 0,
        logFile = logFile
    )
}

typealias ExitCode = Int

private fun runGradleProcess(workingDir: File, logFile: File): ExitCode {
    val ioRedirect = run {
        logFile.delete()
        ProcessBuilder.Redirect.appendTo(logFile)
    }
    val process = ProcessBuilder("./gradlew",
        "--no-daemon",
        "--warning-mode=none",
        "clean", "test"
    )
        .directory(workingDir)
        .redirectOutput(ioRedirect)
        .redirectError(ioRedirect)
        .start()

    return process.waitFor()
}

private fun buildExecutionResult(workingDir: File, buildResult: BuildResult): ExecutionResult {
    val testsDir = workingDir.resolve("build/test-results/test/")

    if (buildResult.failed && !testsDir.exists()) { // probably compilation error
        fun String.removeWorkingDirPath() =
            replace("${workingDir.absolutePath}/", "")

        // FIXME parsing logs is not a very good idea
        //       but it seems that there is no easy way to get
        //       build machine-readable build summary from gradle now
        val message = buildResult.logFile.readLines()
            .asSequence()
            .dropWhile { !it.contains("compileKotlin FAILED") }
            .drop(1)
            .takeWhile { !it.startsWith("FAILURE") }
            .map(String::removeWorkingDirPath)
            .joinToString("\n")
            .trim()

        return ExecutionResult.CompilationFailed(message)
    }

    val suits = testsDir.listJUnitResultFiles()
        .map(::parseJUnit4Results)
    return ExecutionResult.TestsFinished(
        isSuccessful = buildResult.succeeded,
        suits = suits
    )
}

private fun File.listJUnitResultFiles(): List<File> {
    val files = listFiles() ?: return emptyList()

    return files
        .filter { it.isFile }
        .filter { it.name.startsWith("TEST") }
        .filter { it.extension == "xml" }
}
