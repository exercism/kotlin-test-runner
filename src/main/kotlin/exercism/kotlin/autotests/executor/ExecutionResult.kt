package exercism.kotlin.autotests.executor

import utils.junit.TestSuit

sealed class ExecutionResult {
    data class CompilationFailed(val message: String) : ExecutionResult()
    data class TestsFinished(val isSuccessful: Boolean, val suits: List<TestSuit>) : ExecutionResult()
}
