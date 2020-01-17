package runner

import executor.RuntimeConfiguration
import java.io.File

fun buildRuntimeConfigFrom(args: Args): RuntimeConfiguration {
    val exercismRepo = File("../exercism-kotlin")

    val selectedExerciseDir = exercismRepo
        .resolve("exercises")
        .resolve(args.exerciseSlug)
    with(selectedExerciseDir) {
        check(exists()) { "Exercise directory '$absolutePath' not found" }
        check(isDirectory) { "Exercise directory '$absolutePath' is a file" }
    }

    return buildRuntimeConfig(
        exerciseDir = selectedExerciseDir,
        solutionsDir = args.solutionsDir,
        templateDir = exercismRepo.resolve("_template")
    )
}

private fun buildRuntimeConfig(exerciseDir: File, solutionsDir: File, templateDir: File): RuntimeConfiguration {
    return object : RuntimeConfiguration {
        override val workingDirRoot = File("out")
        override val sourcesDir = solutionsDir.resolve("src/main/kotlin")
        override val testsDir = exerciseDir.resolve("src/test/kotlin")
        override val templateDir = templateDir

        // Debug
        override val purgeExistingWorkingDirBefore = true
        override val keepWorkingDirAfter = true
    }
}
