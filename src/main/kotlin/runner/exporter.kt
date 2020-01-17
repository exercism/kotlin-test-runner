package runner

import com.tripl3dogdare.havenjson.Json
import executor.ExecutionResult

fun ExecutionResult.asJson(): String {
    return Json(
        "status" to statusString(),
        "message" to null,
        "tests" to emptyList<Any>()
    ).mkString()
}

private fun ExecutionResult.statusString(): String =
    when (this) {
        ExecutionResult.Success -> "pass"
        ExecutionResult.Fail -> "fail"
        ExecutionResult.Error -> "error"
    }
