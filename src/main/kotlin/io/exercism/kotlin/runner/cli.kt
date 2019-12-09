package io.exercism.kotlin.runner

import java.nio.file.Path

internal fun parseArguments(args: Array<String>): ExecutionArguments {
    require(args.size == 3)

    return ExecutionArguments(
        exerciseSlug = args[0],
        pathToSources = args[1].let { Path.of(it) },
        outputDir = args[2].let { Path.of(it) }
    )
}

data class ExecutionArguments(
    val exerciseSlug: String,
    val pathToSources: Path,
    val outputDir: Path
)
