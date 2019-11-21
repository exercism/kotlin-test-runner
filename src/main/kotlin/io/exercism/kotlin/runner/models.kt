package io.exercism.kotlin.runner

data class ExecutionResult(
    val status: ExecutionStatus,
    val message: String,
    val tests: List<TestExecutionResult>
)

data class TestExecutionResult(
    val name: String,
    val status: ExecutionStatus,
    val message: String
)

sealed class ExecutionStatus(val value: String) {
    object Success : ExecutionStatus("success")
    object Fail : ExecutionStatus("fail")
    object Error : ExecutionStatus("error")

    companion object
}

/*fun ExecutionStatus.Companion.fromValue(value: String?): ExecutionStatus? {
    TODO()
}*/
