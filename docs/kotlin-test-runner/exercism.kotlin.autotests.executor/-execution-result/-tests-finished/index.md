[kotlin-test-runner](../../../index.md) / [exercism.kotlin.autotests.executor](../../index.md) / [ExecutionResult](../index.md) / [TestsFinished](./index.md)

# TestsFinished

`data class TestsFinished : `[`ExecutionResult`](../index.md)

Means that source code compiled and tests finished.
Found tests suits details are provided in [suits](suits.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Means that source code compiled and tests finished. Found tests suits details are provided in [suits](suits.md).`TestsFinished(isSuccessful: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, suits: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TestSuit`](../../../utils.junit/-test-suit/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [isSuccessful](is-successful.md) | `val isSuccessful: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [suits](suits.md) | `val suits: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TestSuit`](../../../utils.junit/-test-suit/index.md)`>` |
