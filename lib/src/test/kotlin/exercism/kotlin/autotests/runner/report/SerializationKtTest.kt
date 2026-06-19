package exercism.kotlin.autotests.runner.report

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SerializationKtTest : StringSpec({

    "passed empty report" {
        val report = Report(
            version = 1,
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
            version = 1,
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
            version = 2,
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
