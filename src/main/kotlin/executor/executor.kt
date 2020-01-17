package executor

import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.nio.file.Files

fun execute(env: Environment) {
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
    executeTests(env.workingDir)

    // Prepare report
}

private fun executeTests(workingDir: File) {
    println("Running gradle")

    val process = ProcessBuilder("./gradlew", "test")
        .directory(workingDir)
        .redirectOutput(Redirect.INHERIT)
        .redirectError(Redirect.INHERIT)
        .start()

    val exitValue = process.waitFor()

    if (exitValue == 0) println("Success")
    else println("Fail")
}
