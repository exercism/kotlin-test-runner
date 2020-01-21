[kotlin-test-runner](../index.md) / [exercism.kotlin.autotests.runner.report](./index.md)

## Package exercism.kotlin.autotests.runner.report

### Types

| Name | Summary |
|---|---|
| [Report](-report/index.md) | Report summary`data class Report` |

### Functions

| Name | Summary |
|---|---|
| [asJson](as-json.md) | Convert report to pretty-printed json string`fun `[`Report`](-report/index.md)`.asJson(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [asReport](as-report.md) | `fun `[`ExecutionResult`](../exercism.kotlin.autotests.executor/-execution-result/index.md)`.asReport(): `[`Report`](-report/index.md) |
| [exportReportToFile](export-report-to-file.md) | Write report to json [file](export-report-to-file.md#exercism.kotlin.autotests.runner.report$exportReportToFile(exercism.kotlin.autotests.executor.ExecutionResult, java.io.File)/file).`fun `[`ExecutionResult`](../exercism.kotlin.autotests.executor/-execution-result/index.md)`.exportReportToFile(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
