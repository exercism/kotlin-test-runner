package exercism.kotlin.autotests

import exercism.kotlin.autotests.runner.main
import io.kotlintest.shouldBe
import io.kotlintest.specs.AbstractStringSpec
import io.kotlintest.specs.StringSpec
import java.io.File
import java.nio.file.Files

class E2ETests : StringSpec({
    testCase("single passing test", "1")
})

private fun AbstractStringSpec.testCase(name: String, id: String) {
    name {
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
}
