package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.runner.report.exportReportToFile
import utils.junit.TestCase
import utils.junit.TestSuit
import java.io.File
import java.net.URLClassLoader

/**
 * Compile and test the solution without Maven, inside this JVM.
 *
 * Maven spends most of each run starting itself, starting a Kotlin compiler daemon and forking a JVM for
 * Surefire. Here the Kotlin compiler and the JUnit Platform run in this JVM with the arguments and classpaths
 * Maven would use, which were recorded at image build (see the Dockerfile).
 *
 * Returns null when the result could differ from Maven's: an unusual project layout, unusual compiler
 * output, or tests that Surefire would report in a special way.
 * The caller then runs Maven as before.
 */
fun fastExecutor(env: Environment): ExecutionResult? {
    val config = FastPathConfig.load() ?: return null
    val workingDir = env.workingDir
    if (!hasPlainLayout(workingDir)) return null

    println("Compiling and testing without Maven")

    val classes = workingDir.resolve("target/classes")
    val testClasses = workingDir.resolve("target/test-classes")
    val compiled = compileKotlin(workingDir.resolve("src/main/kotlin"), classes, config.mainClasspath)
        .then {
            compileKotlin(
                workingDir.resolve("src/test/kotlin"), testClasses,
                listOf(classes.path) + config.testClasspath,
                "-Xfriend-paths=${classes.path}"
            )
        }
    when (compiled) {
        is CompileResult.Failed -> return ExecutionResult.CompilationFailed(compiled.mavenMessage(workingDir))
        CompileResult.Unsupported -> return null.also { println("Unusual compiler output, falling back to Maven") }
        CompileResult.Ok -> {}
    }

    val testClass = findTestClass(testClasses) ?: return null.also {
        println("The tests are not in exactly one test class, falling back to Maven")
    }
    val classpath = listOf(testClasses, classes) + (config.testClasspath + config.harnessClasspath).map(::File)
    val start = System.currentTimeMillis()
    val results = runTests(testClass, classpath, env)
    println("Ran $testClass in ${System.currentTimeMillis() - start} ms")
    if (results == null) return null.also {
        println("Tests need Surefire's own reporting, falling back to Maven")
    }

    val testCases = results.map { (name, status, message, type) ->
        TestCase(
            name = name,
            className = testClass,
            time = "",
            isSkipped = false,
            failure = if (status == "fail") TestCase.Failure(message, type, "") else null,
            error = if (status == "error") TestCase.Error(message, type, "") else null
        )
    }
    val suit = TestSuit(
        name = testClass,
        tests = testCases.size,
        skipped = 0,
        failures = testCases.count { it.failure != null },
        errors = testCases.count { it.error != null },
        testCases = testCases
    )
    return ExecutionResult.TestsFinished(
        isSuccessful = testCases.all { it.failure == null && it.error == null },
        suits = listOf(suit)
    )
}

/** Files written at image build: the classpaths Maven resolves for the exercise pom.xml. */
private class FastPathConfig(
    val mainClasspath: List<String>,
    val testClasspath: List<String>,
    val harnessClasspath: List<String>
) {
    companion object {
        private val dir = File("/opt/test-runner/fast")

        fun load(): FastPathConfig? {
            if (!dir.isDirectory) return null
            fun read(name: String) = dir.resolve(name).readText().trim().split(File.pathSeparator)
            return FastPathConfig(read("main.classpath"), read("test.classpath"), read("harness.classpath"))
        }
    }
}

/**
 * Accept only the layout of the track's exercises: Kotlin files in `src/main/kotlin` and `src/test/kotlin`,
 * no Java files, no Kotlin scripts and no resources. Maven handles anything else.
 */
private fun hasPlainLayout(workingDir: File): Boolean {
    val src = workingDir.resolve("src")
    val main = src.resolve("main/kotlin")
    val test = src.resolve("test/kotlin")
    if (workingDir.resolve("target").exists()) return false
    if (listOf("main/java", "test/java", "main/resources", "test/resources").any { src.resolve(it).exists() }) {
        return false
    }
    val files = src.walk().filter { it.isFile }.toList()
    if (files.any { it.extension == "java" || it.extension == "kts" }) return false
    fun hasKotlin(dir: File) = files.any { it.extension == "kt" && it.startsWith(dir) }
    return hasKotlin(main) && hasKotlin(test)
}

/**
 * Find the class Surefire would run, using its default includes (`Test*`, `*Test`, `*Tests`, `*TestCase`)
 * and excluding nested classes. Returns null unless there is exactly one, so that the report has the
 * same single test suite as Surefire's.
 */
private fun findTestClass(testClasses: File): String? {
    val patterns = listOf(Regex("Test.*"), Regex(".*Test"), Regex(".*Tests"), Regex(".*TestCase"))
    val candidates = testClasses.walk()
        .filter { it.isFile && it.extension == "class" && '$' !in it.name }
        .map { it.relativeTo(testClasses).path.removeSuffix(".class").replace(File.separatorChar, '.') }
        .filter { name -> patterns.any { it.matches(name.substringAfterLast('.')) } }
        .toList()
    return candidates.singleOrNull()
}

private data class FastTestResult(val name: String, val status: String, val message: String, val type: String)

/**
 * Run the tests in a class loader holding only the exercise's test classpath, as Surefire's forked JVM would.
 * If a test calls `System.exit`, a shutdown hook runs Maven instead and writes its report.
 */
private fun runTests(testClass: String, classpath: List<File>, env: Environment): List<FastTestResult>? {
    val loader = URLClassLoader(classpath.map { it.toURI().toURL() }.toTypedArray(), ClassLoader.getPlatformClassLoader())
    val fallbackOnExit = Thread {
        println("A test exited the JVM, falling back to Maven")
        mavenExecutor(env).exportReportToFile(env.resultFile)
    }
    val thread = Thread.currentThread()
    val previousLoader = thread.contextClassLoader
    Runtime.getRuntime().addShutdownHook(fallbackOnExit)
    try {
        thread.contextClassLoader = loader
        val harness = Class.forName("FastTestHarness", true, loader)

        @Suppress("UNCHECKED_CAST")
        val results = harness.getMethod("run", String::class.java).invoke(null, testClass) as List<Array<String>>?
        return results?.map { FastTestResult(it[0], it[1], it[2], it[3]) }
    } finally {
        thread.contextClassLoader = previousLoader
        Runtime.getRuntime().removeShutdownHook(fallbackOnExit)
    }
}
