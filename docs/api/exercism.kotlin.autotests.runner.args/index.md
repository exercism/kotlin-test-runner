[api](../index.md) / [exercism.kotlin.autotests.runner.args](./index.md)

## Package exercism.kotlin.autotests.runner.args

### Types

| [LaunchArguments](-launch-arguments/index.md) | Data structure to keep resolved launch arguments`data class LaunchArguments` |

### Companion Object Functions

| [parseAndValidate](parse-and-validate.md) | Parse launch arguments and validate them. [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) can be thrown if some arguments are invalid or not provided.`fun LaunchArguments.Companion.parseAndValidate(arguments: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`LaunchArguments`](-launch-arguments/index.md) |

