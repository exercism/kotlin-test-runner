package exercism.kotlin.autotests.runner.report

import exercism.kotlin.autotests.executor.ExecutionResult
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Error
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Fail
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Success
import utils.junit.TestSuit
import java.io.File

fun ExecutionResult.exportReportToFile(file: File) {
    val report = asReport()
    file.writeText(report.asJson())
}

private fun ExecutionResult.asReport(): Report {
    return Report(
        status = when (status) {
            Success -> Report.Status.Pass
            Fail -> Report.Status.Fail
            Error -> Report.Status.Error
        },
        message = null,
        tests = suits.asTestEntries()
    )
}

private fun List<TestSuit>.asTestEntries(): List<Report.TestEntry> {
    return flatMap { it.testCases }
        .map { case ->
            Report.TestEntry(
                name = case.name,
                status = if (case.failure == null) Report.Status.Pass else Report.Status.Fail,
                message = case.failure?.message?.lines()?.firstOrNull() ?: "",
                output = case.failure?.message ?: ""
            )
        }
}
