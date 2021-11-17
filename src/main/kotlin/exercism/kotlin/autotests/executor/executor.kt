package exercism.kotlin.autotests.executor

import utils.junit.parseJUnit4Results
import java.io.File

/**
 * Build sources, run tests with `maven` and parse JUnit xml-report.
 */
fun executor(env: Environment): ExecutionResult {
    val buildResult = executeBuild(env.workingDir)

    return buildExecutionResult(env.workingDir, buildResult)
}

/**
 * Run `mvn test` and write maven logs to
 * external file (see implementation for details).
 *
 * Maven logs will also be printed to stdout between special markers:
 * ```
 * === Log START ===
 * ...
 * === Log END ===
 * ```
 */
private fun executeBuild(workingDir: File): BuildResult {
    println("Running Maven")

    val logFile = workingDir.resolve("__out.log")
    val exitCode = runMavenProcess(workingDir, logFile)

    println("Maven finished with exit code $exitCode")
    println("=== Log START ===")
    println(logFile.readText())
    println("=== Log END ===")

    return BuildResult(
        succeeded = exitCode == 0,
        logFile = logFile
    )
}

typealias ExitCode = Int

/**
 * Run `mvn test` from [workingDir] with some parameters.
 * Process `stdout` and `stderr` are written to [logFile].
 *
 * @return exit code of Maven build process.
 */
private fun runMavenProcess(workingDir: File, logFile: File): ExitCode {
    val ioRedirect = run {
        logFile.delete()
        ProcessBuilder.Redirect.appendTo(logFile)
    }
    val process = ProcessBuilder("mvn",
        "test",
        "--offline",
        "--legacy-local-repository",
        "--batch-mode",
        "--non-recursive"
    )
        .directory(workingDir)
        .redirectOutput(ioRedirect)
        .redirectError(ioRedirect)
        .start()

    return process.waitFor()
}

/**
 * Parse JUnit4 xml-report and build [ExecutionResult].
 *
 * If compilation was failed - error is cut-out from maven logs,
 * cleared from full path (`src/main/kotlin/Foo.kt` only left) and used as an error message.
 */
private fun buildExecutionResult(workingDir: File, buildResult: BuildResult): ExecutionResult {
    val testsDir = workingDir.resolve("target/surefire-reports")

    if (buildResult.failed && !testsDir.exists()) { // probably compilation error
        fun String.removeWorkingDirPath() =
            replace("${workingDir.absolutePath}/", "")

        // FIXME parsing logs is not a very good idea
        //       but it seems that there is no easy way to get
        //       build machine-readable build summary from maven now
        val message = buildResult.logFile.readLines()
            .asSequence()
            .dropWhile { !it.contains("BUILD FAILURE") }
            .dropWhile { !it.startsWith("[ERROR]") }
            .drop(1)
            .takeWhile { !it.startsWith("[ERROR] -> [Help 1]") }
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

/** Search for JUnit test suit report files */
private fun File.listJUnitResultFiles(): List<File> {
    val files = listFiles() ?: return emptyList()

    return files
        .filter { it.isFile }
        .filter { it.name.startsWith("TEST") }
        .filter { it.extension == "xml" }
}

data class BuildResult(
    val succeeded: Boolean,
    val logFile: File
)

val BuildResult.failed get() = !succeeded
