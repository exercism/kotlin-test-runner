package exercism.kotlin.autotests.runner.report

import exercism.kotlin.autotests.executor.ExecutionResult
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import utils.junit.TestCase
import utils.junit.TestSuit

class ExporterKtTest : StringSpec({

    "compilation failed with message" {
        val result = ExecutionResult.CompilationFailed("something went wrong")

        val report = result.asReport()

        report.status shouldBe Report.Status.Error
        report.message shouldBe "something went wrong"
        report.tests shouldBe emptyList()
    }

    "all tests passed" {
        val result = ExecutionResult.TestsFinished(
            isSuccessful = true,
//            suits = listOf(createTestSuit("test-suit-1", listOf("test-1", "test-2"))))
            suits = emptyList())

        val report = result.asReport()

        report.status shouldBe Report.Status.Pass
        report.message shouldBe ""
        report.tests shouldBe emptyList()
    }

    "some tests failed" {
        val result = ExecutionResult.TestsFinished(
            isSuccessful = false,
            suits = emptyList())

        val report = result.asReport()

        report.status shouldBe Report.Status.Fail
        report.message shouldBe ""
        report.tests shouldBe emptyList()
    }

    /*"some tests skipped" {
        val result = ExecutionResult.TestsFinished(
            isSuccessful = true,
            suits = emptyList())

        val report = result.asReport()

        report.status shouldBe Report.Status.Pass
        report.message shouldBe ""
        report.tests shouldBe emptyList()
    }*/
})

private fun createTestSuit(name: String, testNames: List<String>) =
    TestSuit(name, 0, 0, 0, 0, "", "", "", "", "", testNames.map { TestCase(it, "", "", false, null) })
