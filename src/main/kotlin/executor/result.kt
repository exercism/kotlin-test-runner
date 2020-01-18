package executor

import junit.TestSuit

data class ExecutionResult(
    val status: Status,
    val suits: List<TestSuit> = emptyList()
) {

    enum class Status {
        Success,
        Fail,
        Error
    }
}
