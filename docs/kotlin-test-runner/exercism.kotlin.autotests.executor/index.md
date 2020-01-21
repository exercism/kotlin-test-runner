[kotlin-test-runner](../index.md) / [exercism.kotlin.autotests.executor](./index.md)

## Package exercism.kotlin.autotests.executor

### Types

| Name | Summary |
|---|---|
| [BuildResult](-build-result/index.md) | `data class BuildResult` |
| [Environment](-environment/index.md) | `data class Environment` |
| [ExecutionResult](-execution-result/index.md) | Result of tests execution process.`sealed class ExecutionResult` |
| [ExitCode](-exit-code.md) | `typealias ExitCode = `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Properties

| Name | Summary |
|---|---|
| [failed](failed.md) | `val `[`BuildResult`](-build-result/index.md)`.failed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [executeOnEnvironment](execute-on-environment.md) | Make some preparations before running build process (setup environment), launch [executor](execute-on-environment.md#exercism.kotlin.autotests.executor$executeOnEnvironment(exercism.kotlin.autotests.runner.args.LaunchArguments, kotlin.Function1((exercism.kotlin.autotests.executor.Environment, exercism.kotlin.autotests.executor.ExecutionResult)))/executor) and clean-up after.`fun executeOnEnvironment(args: `[`LaunchArguments`](../exercism.kotlin.autotests.runner.args/-launch-arguments/index.md)`, executor: (`[`Environment`](-environment/index.md)`) -> `[`ExecutionResult`](-execution-result/index.md)`): `[`ExecutionResult`](-execution-result/index.md) |
| [executor](executor.md) | Build sources, run tests with `./gradlew` and parse JUnit xml-report.`fun executor(env: `[`Environment`](-environment/index.md)`): `[`ExecutionResult`](-execution-result/index.md) |
