package executor

sealed class ExecutionResult {
    object Success : ExecutionResult()
    object Fail : ExecutionResult()
    object Error : ExecutionResult()
}
