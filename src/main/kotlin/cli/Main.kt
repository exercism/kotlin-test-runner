package cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import executor.execute
import executor.withEnvironment
import java.io.File

// run --sources example/src --tests example/test --template ../exercism-kotlin/_template
fun main(args: Array<String>) {
    val launchArguments = parseArguments(args)

    println("""
            Parsed:
              [Argument] Sources: '${launchArguments.sourcesDir.absolutePath}
              [Argument] Tests: '${launchArguments.testsDir.absolutePath}
              [Argument] Template: '${launchArguments.templateDir.absolutePath}
            """.trimIndent())

    withEnvironment(launchArguments, ::execute)
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
