[kotlin-test-runner](../../index.md) / [exercism.kotlin.autotests.runner.report](../index.md) / [Report](./index.md)

# Report

`data class Report`

Report summary

### Types

| Name | Summary |
|---|---|
| [Status](-status/index.md) | Testing process status. [id](-status/id.md) is used as value for `results.json`.`enum class Status` |
| [TestEntry](-test-entry/index.md) | `data class TestEntry` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Report summary`Report(status: Status, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, tests: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<TestEntry>)` |

### Properties

| Name | Summary |
|---|---|
| [message](message.md) | `val message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [status](status.md) | `val status: Status` |
| [tests](tests.md) | `val tests: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<TestEntry>` |

### Extension Functions

| Name | Summary |
|---|---|
| [asJson](../as-json.md) | Convert report to pretty-printed json string`fun `[`Report`](./index.md)`.asJson(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
