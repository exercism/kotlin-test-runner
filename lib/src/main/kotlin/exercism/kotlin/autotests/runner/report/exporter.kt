package exercism.kotlin.autotests.runner.report

import exercism.kotlin.autotests.executor.ExecutionResult
import exercism.kotlin.autotests.executor.ExecutionResult.CompilationFailed
import exercism.kotlin.autotests.executor.ExecutionResult.TestsFinished
import utils.junit.TestCase
import utils.junit.TestSuit
import java.io.File

/**
 * Write report to json [file].
 */
fun ExecutionResult.exportReportToFile(file: File) {
    val report = asReport()
    file.writeText(report.asJson())
}

fun ExecutionResult.asReport(): Report =
    Report(
        version = 2,
        status = parseStatus(),
        message = parseMessage(),
        tests = parseTestEntries()
    )

private fun ExecutionResult.parseStatus(): Report.Status =
    when (this) {
        is CompilationFailed ->
            Report.Status.Error
        is TestsFinished ->
            if (isSuccessful) Report.Status.Pass else Report.Status.Fail
    }

private fun ExecutionResult.parseMessage(): String =
    when (this) {
        is CompilationFailed ->
            message
        is TestsFinished ->
            ""
    }

private fun ExecutionResult.parseTestEntries(): List<Report.TestEntry> =
    when (this) {
        is CompilationFailed ->
            emptyList()
        is TestsFinished ->
            suits.asReportTestEntries()
    }

private fun List<TestSuit>.asReportTestEntries(): List<Report.TestEntry> =
    flatMap { it.testCases }
        .filterNot { it.isSkipped }
        .map(TestCase::asReportTestEntry)

private fun TestCase.asReportTestEntry(): Report.TestEntry =
    Report.TestEntry(
        name = name,
        status = when {
            error != null -> Report.Status.Error
            failure != null -> Report.Status.Fail
            else -> Report.Status.Pass
        },
        message = getMessage(),
        output = "" // TODO output is available only for test suit in JUnit4
    )

private fun TestCase.getMessage(): String {
    if (error != null) return error.message
    if (failure == null) return ""

    val rawMessage = failure.message
    return when (failure.type) {
        "org.junit.ComparisonFailure" -> rawMessage.substringAfter("org.junit.ComparisonFailure:").trim()
        else -> rawMessage
    }
}
