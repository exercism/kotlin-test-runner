package exercism.kotlin.autotests.runner.report

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SerializationKtTest : StringSpec({

    "passed empty report" {
        val report = Report(
            status = Report.Status.Pass,
            message = null,
            tests = emptyList()
        )

        report.asJson() shouldBe """
            {
              "status": "pass",
              "tests": []
            }
        """.trimIndent()
    }

    "failed empty report" {
        val report = Report(
            status = Report.Status.Fail,
            message = null,
            tests = emptyList()
        )

        report.asJson() shouldBe """
            {
              "status": "fail",
              "tests": []
            }
        """.trimIndent()
    }

    "crashed empty report" {
        val report = Report(
            status = Report.Status.Error,
            message = null,
            tests = emptyList()
        )

        report.asJson() shouldBe """
            {
              "status": "error",
              "tests": []
            }
        """.trimIndent()
    }
})
