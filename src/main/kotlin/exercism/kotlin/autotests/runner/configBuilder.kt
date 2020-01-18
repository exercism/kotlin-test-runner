package exercism.kotlin.autotests.runner

import exercism.kotlin.autotests.executor.RuntimeConfiguration
import exercism.kotlin.autotests.runner.args.LaunchArguments
import java.io.File

fun buildRuntimeConfigFrom(args: LaunchArguments): RuntimeConfiguration {
    val exercismRepo = File("../exercism-kotlin")

    val selectedExerciseDir = exercismRepo
        .resolve("exercises")
        .resolve(args.exerciseSlug)
//    with(selectedExerciseDir) {
//        check(exists()) { "Exercise directory '$absolutePath' not found" }
//        check(isDirectory) { "Exercise directory '$absolutePath' is a file" }
//    }

    return buildRuntimeConfig(
        args = args,
        exerciseDir = selectedExerciseDir,
        solutionsDir = args.solutionsDir,
        templateDir = exercismRepo.resolve("_template")
    )
}

private fun buildRuntimeConfig(args: LaunchArguments, exerciseDir: File, solutionsDir: File, templateDir: File): RuntimeConfiguration {
    return object : RuntimeConfiguration {
        override val workingDirRoot = File("out")
        override val sourcesDir = solutionsDir.resolve("src/main/kotlin")
        override val testsDir = exerciseDir.resolve("src/test/kotlin")
        override val templateDir = templateDir

        override val args = args

        // Debug
        override val purgeExistingWorkingDirBefore = true
        override val keepWorkingDirAfter = true
    }
}
