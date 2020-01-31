package exercism

import exercism.TestItem.ExecutionResult
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status.ABORTED
import org.junit.platform.engine.TestExecutionResult.Status.FAILED
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class TestReporter : TestExecutionListener {

    private val stdOut = ByteArrayOutputStream()
    private val stdErr = ByteArrayOutputStream()

    private val stdOutStream = PrintStream(stdOut)
    private val stdErrStream = PrintStream(stdErr)

    private val items = mutableListOf<TestItem>()

    private var testPlans = 0

    override fun testPlanExecutionStarted(testPlan: TestPlan?) {
        testPlans++
    }

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        testPlans--

        if (testPlans == 0) {
            File("build/out.txt").writeText(
                items.joinToString("\n")
            )
        }
    }

    override fun executionStarted(identifier: TestIdentifier) {
        if (!identifier.isTest) return

        stdOut.reset()
        stdErr.reset()

        System.setOut(stdOutStream)
        System.setErr(stdErrStream)
    }

    override fun executionFinished(identifier: TestIdentifier, result: TestExecutionResult) {
        if (!identifier.isTest) return

        recordTestItem(identifier, result.parseExecutionResult())
    }

    override fun executionSkipped(identifier: TestIdentifier, reason: String) {
        if (!identifier.isTest) return

        recordTestItem(identifier, ExecutionResult.Skipped(reason))
    }

    private fun recordTestItem(identifier: TestIdentifier, result: ExecutionResult) {
        items += TestItem(
            name = identifier.displayName.removeSuffix("()"),
            result = result,
            stdOut = stdOut.toString().trim(),
            stdErr = stdErr.toString().trim()
        )
    }
}

data class TestItem(val name: String, val result: ExecutionResult, val stdOut: String, val stdErr: String) {

    sealed class ExecutionResult {
        object Successful : ExecutionResult()
        data class Skipped(val reason: String) : ExecutionResult()
        data class Aborted(val throwable: Throwable?) : ExecutionResult()
        data class Failed(val throwable: Throwable?) : ExecutionResult()
    }
}

fun TestExecutionResult.parseExecutionResult(): ExecutionResult = when (status!!) {
    SUCCESSFUL -> ExecutionResult.Successful
    FAILED -> ExecutionResult.Failed(throwable.orElse(null))
    ABORTED -> ExecutionResult.Aborted(throwable.orElse(null))
}
