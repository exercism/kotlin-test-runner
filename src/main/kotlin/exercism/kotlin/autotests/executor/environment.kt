package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.runner.BuildConfig
import exercism.kotlin.autotests.runner.args.LaunchArguments
import utils.joinAsText
import java.io.File

/**
 * Make some preparations before running build process (setup environment), launch [executor] and clean-up after.
 *
 * @return test process [ExecutionResult]
 */
fun executeOnEnvironment(args: LaunchArguments, executor: (Environment) -> ExecutionResult): ExecutionResult {
    val env = setupEnvironment(args)
    val result = executor(env)
    env.tearDown()

    return result
}

/**
 * Resolve [LaunchArguments] to useful [Environment] parameters and
 * prepare working directory inside solutions directory to avoid overwriting files.
 */
private fun setupEnvironment(args: LaunchArguments): Environment {
    val env = Environment(
        workingDir = args.solutionsDir.resolve("out/"),
        resultFile = args.resultFile
    )

    copyFilesToWorkingDir(args.solutionsDir, env.workingDir)
    cleanupTests(env.workingDir)
    env.workingDir.resolve("gradlew").setExecutable(true)

    env.resultFile.parentFile.mkdirs()

    return env
}

/** Remove working directory unless [BuildConfig.keepWorkingDir] is not set. */
private fun Environment.tearDown() {
    if (!BuildConfig.keepWorkingDir) {
        workingDir.deleteRecursively()
    }
}

private fun copyFilesToWorkingDir(solutionsDir: File, workingDir: File) {
    fun File.copyToWorkingDir() {
        copyRecursively(workingDir.resolve(name))
    }

    workingDir.deleteRecursively()
    solutionsDir.listFiles()!!
        .forEach(File::copyToWorkingDir)
}

/** Remove `@Ignore` annotation in tests. */
private fun cleanupTests(workingDir: File) {
    fun File.isTestFile() = name.endsWith("Test.kt")
    fun List<String>.filterIgnoreLines() = filterNot { it.trim() == "@Ignore" }

    workingDir.resolve("src/test/kotlin")
        .walk()
        .toList()
        .filter(File::isTestFile)
        .forEach { file ->
            val newContent = file
                .readLines()
                .filterIgnoreLines()
                .joinAsText()

            file.writeText(newContent)
        }
}

data class Environment(
    /** Temporary working directory that is copy of solutions directory but can be safely modified. */
    val workingDir: File,
    /** Reference to `results.json` [File] that suppose to be written in the end of testing process. */
    val resultFile: File
)
