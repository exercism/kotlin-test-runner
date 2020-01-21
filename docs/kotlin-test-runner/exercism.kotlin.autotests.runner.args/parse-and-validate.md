[kotlin-test-runner](../index.md) / [exercism.kotlin.autotests.runner.args](index.md) / [parseAndValidate](./parse-and-validate.md)

# parseAndValidate

`fun LaunchArguments.Companion.parseAndValidate(arguments: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`LaunchArguments`](-launch-arguments/index.md)

Parse launch arguments and validate them. [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) can be thrown if some arguments are invalid or not provided.

Typically used in `main()`:

``` kotlin
fun main(args: Array<String>) {
  val parsedArguments = LaunchArguments.parseAndValidate(args)
  ...
}
```

