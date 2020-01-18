package runner

import com.tripl3dogdare.havenjson.Json
import com.tripl3dogdare.havenjson.JsonArray
import executor.ExecutionResult
import executor.ExecutionResult.Status.Error
import executor.ExecutionResult.Status.Fail
import executor.ExecutionResult.Status.Success
import junit.TestSuit

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
