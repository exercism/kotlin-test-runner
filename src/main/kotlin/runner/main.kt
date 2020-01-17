package runner

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.nio.file.Files

// run --sources example/src --tests example/test --template ../exercism-kotlin/_template

fun main(args: Array<String>) {
    val launchArguments = parseArguments(args)

    println("""
            Parsed:
              [Argument] Sources: '${launchArguments.sourcesDir.absolutePath}
              [Argument] Tests: '${launchArguments.testsDir.absolutePath}
              [Argument] Template: '${launchArguments.templateDir.absolutePath}
            """.trimIndent())

    val env = setupEnvironment(launchArguments)
    execute(env)
    env.tearDown(launchArguments)
}

private fun execute(env: Environment) {
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

private fun setupEnvironment(args: LaunchArguments): Environment {
    // Check arguments
    check(args.sourcesDir.exists()) { "Sources directory '${args.sourcesDir.absolutePath}' does not exist" }
    check(args.testsDir.exists()) { "Tests directory '${args.testsDir.absolutePath}' does not exist" }
    check(args.templateDir.exists()) { "Project template directory '${args.templateDir.absolutePath}' does not exist" }
    //check(args.sourcesDir.isDirectory) { "Sources directory '${args.sourcesDir.absolutePath}' is not a directory" }

    // Create temp dir
    val tempDir = args.workingDirRoot.resolve("__autotest_workingdir")
    if (args.purgeExistingWorkingDirBefore) {
        if (tempDir.exists()) tempDir.deleteRecursively()
    } else {
        check(!tempDir.exists()) { "Directory ${tempDir.absolutePath} exists. Can't continue" }
    }

    println("Creating temp working dir: '${tempDir.absolutePath}'")
    tempDir.mkdirs()

    return Environment(
        workingDir = tempDir,

        sourcesDir = args.sourcesDir,
        testsDir = args.testsDir,
        templateDir = args.templateDir
    )
}

private fun Environment.tearDown(args: LaunchArguments) {
    // Remote temp dir
    if (!args.keepWorkingDirAfter) {
        workingDir.deleteRecursively()
    }
}

private fun parseArguments(args: Array<String>): LaunchArguments =
    ArgParser(args).parseInto(::LaunchArguments)

class LaunchArguments(parser: ArgParser) {
    val workingDirRoot by parser.storing("--workingDirRoot", help = "set root directory for working dir") { File(this) }
        .default(File("."))
    val sourcesDir by parser.storing("--sources", help = "path to sources directory") { File(this) }
    val testsDir by parser.storing("--tests", help = "path to tests directory") { File(this) }
    val templateDir by parser.storing("--template", help = "path to directory with project template") { File(this) }

    val purgeExistingWorkingDirBefore by parser.flagging("--purgeWorkingDir", help = "set to remove existing working dir before run").default(false)
    val keepWorkingDirAfter by parser.flagging("--keepWorkingDir", help = "set to avoid removing created working dir after run")
        .default(false)
}

data class Environment(
    val workingDir: File,
    val sourcesDir: File,
    val testsDir: File,
    val templateDir: File
)


