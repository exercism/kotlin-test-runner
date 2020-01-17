package executor

import cli.LaunchArguments
import java.io.File

fun withEnvironment(args: LaunchArguments, executor: (Environment) -> Unit) {
    val env = setupEnvironment(args)
    executor(env)
    env.tearDown(args)
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

data class Environment(
    val workingDir: File,
    val sourcesDir: File,
    val testsDir: File,
    val templateDir: File
)
