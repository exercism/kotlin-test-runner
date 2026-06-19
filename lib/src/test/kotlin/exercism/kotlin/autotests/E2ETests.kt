package exercism.kotlin.autotests

import exercism.kotlin.autotests.runner.main
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.TestXMethod
import io.kotest.core.test.TestScope
import io.kotest.core.test.TestType
import io.kotest.matchers.shouldBe
import java.io.File
import java.nio.file.Files

class E2ETests : FunSpec({
    context("E2E Tests") {
        registerTest(
            name = TestName(
                name = "single passing test",
                focus = false,
                bang = false,
                prefix = null,
                suffix = null,
                defaultAffixes = false
            ),
            xmethod = TestXMethod.NONE,
            config = null,
            type = TestType.Test,
            test = testCase("1")
        )

        registerTest(
            name = TestName(
                name = "compilation error",
                focus = false,
                bang = false,
                prefix = null,
                suffix = null,
                defaultAffixes = false
            ),
            xmethod = TestXMethod.NONE,
            config = null,
            type = TestType.Test,
            test = testCase("2")
        )

        registerTest(
            name = TestName(
                name = "some tests are failing",
                focus = false,
                bang = false,
                prefix = null,
                suffix = null,
                defaultAffixes = false
            ),
            xmethod = TestXMethod.NONE,
            config = null,
            type = TestType.Test,
            test = testCase("3")
        )

        // TODO add more tests with other cases
    }
})

private fun testCase(id: String): suspend TestScope.() -> Unit = {
    // Prepare working dir
    val workingDir = Files.createTempDirectory("exercism-kotlin-test-runner").toFile()

    try {
        File("../examples/template").copyRecursively(workingDir)
        File("../examples/$id").copyRecursively(workingDir)

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
