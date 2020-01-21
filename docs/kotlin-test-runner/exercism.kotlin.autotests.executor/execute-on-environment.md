[kotlin-test-runner](../index.md) / [exercism.kotlin.autotests.executor](index.md) / [executeOnEnvironment](./execute-on-environment.md)

# executeOnEnvironment

`fun executeOnEnvironment(args: `[`LaunchArguments`](../exercism.kotlin.autotests.runner.args/-launch-arguments/index.md)`, executor: (`[`Environment`](-environment/index.md)`) -> `[`ExecutionResult`](-execution-result/index.md)`): `[`ExecutionResult`](-execution-result/index.md)

Make some preparations before running build process (setup environment), launch [executor](execute-on-environment.md#exercism.kotlin.autotests.executor$executeOnEnvironment(exercism.kotlin.autotests.runner.args.LaunchArguments, kotlin.Function1((exercism.kotlin.autotests.executor.Environment, exercism.kotlin.autotests.executor.ExecutionResult)))/executor) and clean-up after.

**Return**
test process [ExecutionResult](-execution-result/index.md)

