package exercism.kotlin.autotests.runner

import com.tripl3dogdare.havenjson.Json
import com.tripl3dogdare.havenjson.JsonArray
import exercism.kotlin.autotests.executor.ExecutionResult
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Error
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Fail
import exercism.kotlin.autotests.executor.ExecutionResult.Status.Success
import utils.junit.TestSuit

fun ExecutionResult.asJson(): String {
    return Json(
        "status" to statusString(),
        "message" to null,
        "tests" to suits.testsArray()
    ).mkString()
}

private fun ExecutionResult.statusString(): String =
    when (status) {
        Success -> "pass"
        Fail -> "fail"
        Error -> "error"
    }

private fun List<TestSuit>.testsArray(): JsonArray {
    val cases = flatMap { it.testCases }

    return cases.map { case ->
        Json(
            "name" to case.name,
            "status" to (if (case.failure == null) "pass" else "fail"),
            "message" to (case.failure?.message?.lines()?.firstOrNull() ?: ""),
            "output" to (case.failure?.message ?: "")
        )
    }.let { JsonArray(it) }
}
