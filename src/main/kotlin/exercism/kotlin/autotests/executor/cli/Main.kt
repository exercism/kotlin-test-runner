package exercism.kotlin.autotests.executor.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import exercism.kotlin.autotests.executor.RuntimeConfiguration
import exercism.kotlin.autotests.executor.executor
import exercism.kotlin.autotests.executor.executeOnEnvironment
import java.io.File

// run --sources example/src --tests example/test --template ../exercism-kotlin/_template
fun main(args: Array<String>) {
    val config = parseRuntimeConfiguration(args)

    println("""
            Parsed:
              [Argument] Sources: '${config.sourcesDir.absolutePath}
              [Argument] Tests: '${config.testsDir.absolutePath}
              [Argument] Template: '${config.templateDir.absolutePath}
            """.trimIndent())

    executeOnEnvironment(config, ::executor)
}

private fun parseRuntimeConfiguration(args: Array<String>): RuntimeConfiguration =
    ArgParser(args).parseInto(::LaunchArguments)

class LaunchArguments(parser: ArgParser) : RuntimeConfiguration {
    override val workingDirRoot by parser.storing("--workingDirRoot", help = "set root directory for working dir") { File(this) }
        .default(File("."))
    override val sourcesDir by parser.storing("--sources", help = "path to sources directory") { File(this) }
    override val testsDir by parser.storing("--tests", help = "path to tests directory") { File(this) }
    override val templateDir by parser.storing("--template", help = "path to directory with project template") { File(this) }

    override val purgeExistingWorkingDirBefore by parser.flagging("--purgeWorkingDir", help = "set to remove existing working dir before run").default(false)
    override val keepWorkingDirAfter by parser.flagging("--keepWorkingDir", help = "set to avoid removing created working dir after run")
        .default(false)
}
