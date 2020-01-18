package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.runner.BuildConfig
import exercism.kotlin.autotests.runner.args.LaunchArguments
import java.io.File
import java.nio.file.Files

fun executeOnEnvironment(args: LaunchArguments, executor: (Environment) -> ExecutionResult): ExecutionResult {
    val env = setupEnvironment(args)
    val result = executor(env)
    env.tearDown()

    return result
}

private fun setupEnvironment(args: LaunchArguments): Environment {
    val env = run {
        val workingDir = args.solutionsDir
            .resolve("__runner_working_dir")
            .also { it.mkdirs() }

        Environment(
            workingDir = workingDir,
            resultFile = args.resultFile
        )
    }

    copyFilesToWorkingDir(args.solutionsDir, env.workingDir)
    cleanupTests(env.workingDir)

    env.workingDir.resolve("gradlew").setExecutable(true)

    return env
}

private fun Environment.tearDown() {
    if (!BuildConfig.keepWorkingDir) {
        workingDir.deleteRecursively()
    }
}

private fun copyFilesToWorkingDir(solutionsDir: File, workingDir: File) {
    solutionsDir.listFiles()!!
        .toList()
        .minus(workingDir)
        .forEach { file ->
            val destination = workingDir.resolve(file.name)
            file.copyRecursively(destination, overwrite = true, onError = { _, _ -> OnErrorAction.SKIP })
        }
}

private fun cleanupTests(workingDir: File) {
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
}

data class Environment(
    val workingDir: File,
    val resultFile: File
)
