package exercism.kotlin.autotests.executor

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.cli.common.ExitCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

sealed class CompileResult {
    object Ok : CompileResult()

    /** The compiler output can't be turned into Maven's error message, so Maven must run. */
    object Unsupported : CompileResult()

    /** [errors] are the compiler errors, rendered the way the kotlin-maven-plugin renders them. */
    class Failed(val errors: List<String>) : CompileResult() {
        /**
         * Build the message the runner cuts out of Maven's log after a compile error.
         * Maven prints each line of each error prefixed with `[ERROR] `, and the runner then
         * removes the working directory from every line.
         */
        fun mavenMessage(workingDir: File): String =
            errors.flatMap { it.split("\n") }
                .joinToString("\n") { "[ERROR] $it".replace("${workingDir.absolutePath}/", "") }
                .trim()
    }

    fun then(next: () -> CompileResult): CompileResult = if (this == Ok) next() else this
}

/**
 * Run the Kotlin compiler in this JVM, with the same arguments as the kotlin-maven-plugin
 * (as printed by `mvn -X`), and collect its errors.
 */
fun compileKotlin(sourceDir: File, outputDir: File, classpath: List<String>, vararg extraArgs: String): CompileResult {
    val sources = sourceDir.walk().filter { it.isFile && it.extension == "kt" }.map { it.path }.toList()
    val args = listOf(
        "-classpath", classpath.joinToString(File.pathSeparator),
        "-d", outputDir.path,
        *extraArgs,
        "-jvm-target", "1.8",
        "-module-name", "exercise",
        "-no-stdlib"
    ) + sources

    val start = System.currentTimeMillis()
    val renderer = MavenPluginRenderer()
    val output = ByteArrayOutputStream()
    val exitCode = PrintStream(output, true, "UTF-8").use { K2JVMCompiler().exec(it, renderer, *args.toTypedArray()) }
    print(output.toString("UTF-8"))
    println("Compiled $sourceDir in ${System.currentTimeMillis() - start} ms: $exitCode")

    return when {
        exitCode == ExitCode.OK && renderer.errors.isEmpty() -> CompileResult.Ok
        exitCode == ExitCode.COMPILATION_ERROR && renderer.errors.isNotEmpty() && !renderer.hadException ->
            CompileResult.Failed(renderer.errors)
        else -> CompileResult.Unsupported
    }
}

/**
 * Render messages the way the Kotlin Build Tools API does for the kotlin-maven-plugin
 * (its DefaultCompilerMessageRenderer), and keep the errors.
 */
private class MavenPluginRenderer : MessageRenderer {
    val errors = mutableListOf<String>()
    var hadException = false

    override fun render(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?): String {
        val rendered = buildString {
            if (location != null) {
                append(File(location.path).toPath().toUri())
                if (location.line > 0 && location.column > 0) append(":${location.line}:${location.column}")
                append(' ')
            }
            append(message)
        }
        if (severity == CompilerMessageSeverity.EXCEPTION) hadException = true
        if (severity.isError && rendered.isNotBlank()) errors += rendered
        return rendered
    }

    override fun renderPreamble() = ""
    override fun renderUsage(usage: String) = usage
    override fun renderConclusion() = ""
    override fun getName() = "MavenPlugin"
}
