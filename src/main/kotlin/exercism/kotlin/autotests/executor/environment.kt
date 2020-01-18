package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.runner.BuildConfig
import exercism.kotlin.autotests.runner.args.LaunchArguments
import utils.joinAsText
import java.io.File

fun executeOnEnvironment(args: LaunchArguments, executor: (Environment) -> ExecutionResult): ExecutionResult {
    val env = setupEnvironment(args)
    val result = executor(env)
    env.tearDown()

    return result
}

private fun setupEnvironment(args: LaunchArguments): Environment {
    val env = Environment(
        workingDir = args.solutionsDir.resolve("build/__runner_working_dir"),
        resultFile = args.resultFile
    )

    copyFilesToWorkingDir(args.solutionsDir, env.workingDir)
    cleanupTests(env.workingDir)
    env.workingDir.resolve("gradlew").setExecutable(true)

    env.resultFile.parentFile.mkdirs()

    return env
}

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

private fun cleanupTests(workingDir: File) {
    fun File.isTestFile() = name.endsWith("Test.kt")
    fun List<String>.filterIgnoreLines() = filterNot { it.trim() == "@Ignore" }

    workingDir.resolve("src/test/kotlin")
        .walk()
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
    val workingDir: File,
    val resultFile: File
)
