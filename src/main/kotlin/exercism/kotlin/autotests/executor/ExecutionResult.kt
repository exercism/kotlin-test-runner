package exercism.kotlin.autotests.executor

import utils.junit.TestSuit

/**
 * Result of tests execution process.
 */
sealed class ExecutionResult {
    /** Means that tests can't be executed because source code compilation failed */
    data class CompilationFailed(val message: String) : ExecutionResult()

    /**
     * Means that source code compiled and tests finished.
     * Found tests suits details are provided in [suits].
     */
    data class TestsFinished(val isSuccessful: Boolean, val suits: List<TestSuit>) : ExecutionResult()
}
