package exercism.kotlin.autotests

import exercism.kotlin.autotests.runner.main
import io.kotlintest.TestContext
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.nio.file.Files

class E2ETests : StringSpec({
    "single passing test"(testCase("1"))
    "compilation error"(testCase("2"))
    "some tests are failing"(testCase("3"))

    // TODO add more tests with other cases
})

private fun testCase(id: String): suspend TestContext.() -> Unit = {
    // Prepare working dir
    val workingDir = Files.createTempDirectory("exercism-kotlin-test-runner").toFile()

    try {
        File("examples/template").copyRecursively(workingDir)
        File("examples/$id").copyRecursively(workingDir)

        // Execute program
        val outputDir = workingDir.resolve("build/")
        main(arrayOf("exercise-slug", workingDir.absolutePath, outputDir.absolutePath))

        // Assert result
        val actual = outputDir.resolve("results.json").readText().trim()
        val expected = workingDir.resolve("expected_results.json").readText().trim()

        actual shouldBe expected
    } finally {
        workingDir.deleteRecursively()
    }
}
