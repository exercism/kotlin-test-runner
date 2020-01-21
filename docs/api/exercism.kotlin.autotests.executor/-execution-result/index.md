[api](../../index.md) / [exercism.kotlin.autotests.executor](../index.md) / [ExecutionResult](./index.md)

# ExecutionResult

`sealed class ExecutionResult`

Result of tests execution process.

### Types

| [CompilationFailed](-compilation-failed/index.md) | Means that tests can't be executed because source code compilation failed`data class CompilationFailed : `[`ExecutionResult`](./index.md) |
| [TestsFinished](-tests-finished/index.md) | Means that source code compiled and tests finished. Found tests suits details are provided in [suits](-tests-finished/suits.md).`data class TestsFinished : `[`ExecutionResult`](./index.md) |

### Extension Functions

| [asReport](../../exercism.kotlin.autotests.runner.report/as-report.md) | `fun `[`ExecutionResult`](./index.md)`.asReport(): `[`Report`](../../exercism.kotlin.autotests.runner.report/-report/index.md) |
| [exportReportToFile](../../exercism.kotlin.autotests.runner.report/export-report-to-file.md) | Write report to json [file](../../exercism.kotlin.autotests.runner.report/export-report-to-file.md#exercism.kotlin.autotests.runner.report$exportReportToFile(exercism.kotlin.autotests.executor.ExecutionResult, java.io.File)/file).`fun `[`ExecutionResult`](./index.md)`.exportReportToFile(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

