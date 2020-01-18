package exercism.kotlin.autotests.executor

import java.io.File
import java.nio.file.Files

fun executeOnEnvironment(config: RuntimeConfiguration, executor: (Environment) -> ExecutionResult): ExecutionResult {
    val env = setupEnvironment(config)
    val result = executor(env)
    env.tearDown(config)

    return result
}

private fun setupEnvironment(config: RuntimeConfiguration): Environment {
    // Check arguments
//    check(config.sourcesDir.exists()) { "Sources directory '${config.sourcesDir.absolutePath}' does not exist" }
//    check(config.testsDir.exists()) { "Tests directory '${config.testsDir.absolutePath}' does not exist" }
//    check(config.templateDir.exists()) { "Project template directory '${config.templateDir.absolutePath}' does not exist" }
    //check(args.sourcesDir.isDirectory) { "Sources directory '${args.sourcesDir.absolutePath}' is not a directory" }

    // Create temp dir
    val tempDir = config.workingDirRoot.resolve("__autotest_workingdir")
    if (config.purgeExistingWorkingDirBefore) {
        if (tempDir.exists()) tempDir.deleteRecursively()
    } else {
        check(!tempDir.exists()) { "Directory ${tempDir.absolutePath} exists. Can't continue" }
    }

    println("Creating temp working dir: '${tempDir.absolutePath}'")
    tempDir.mkdirs()

    val solutionsDir =  config.args.solutionsDir
    val workingDir = solutionsDir
        .resolve("__runner_working_dir")
        .also { it.mkdirs() }

    solutionsDir.listFiles()!!
        .toList()
        .minus(workingDir)
        .forEach { file ->
            file.copyRecursively(workingDir.resolve(file.name), overwrite = true)
        }
    workingDir.resolve("gradlew").setExecutable(true)

    workingDir.resolve("src/test/kotlin").toPath()
        .let { Files.walk(it) }
        .filter { it.endsWith("Test.kt") }
        .map { it.toFile() }
        .filter { it.readLines().any { line -> line.contains("@Ignore") } }
        .forEach { file ->
            val newContent = file
                .readLines()
                .filterNot { it.trim() == "@Ignore" }

            file.writeText(newContent.joinToString("\n"))
        }

    return Environment(
        workingDir = workingDir,
        resultFile = config.args.resultFile

//        ,
//        workingDir = tempDir,
//
//        sourcesDir = config.sourcesDir,
//        testsDir = config.testsDir,
//        templateDir = config.templateDir
    )
}

private fun Environment.tearDown(config: RuntimeConfiguration) {
    // Remote temp dir
    if (!config.keepWorkingDirAfter) {
        //workingDir.deleteRecursively()
    }
}

data class Environment(
    val workingDir: File,
    val resultFile: File

//    ,
//    val workingDir: File,
//    val sourcesDir: File,
//    val testsDir: File,
//    val templateDir: File
)
