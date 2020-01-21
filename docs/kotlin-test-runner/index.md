[kotlin-test-runner](./index.md)

Test Runner has few stages:

1. parsing `LaunchArguments`;
2. preparing environment (e.g. creating temporary working directory, copying source files to working directory, cleaning up tests etc);
3. running `./gradlew test` in working directory to execute tests;
4. parsing JUnit test results;
5. converting test results to [predefined report structure](https://github.com/exercism/automated-tests/blob/master/docs/interface.md);
6. marshaling report to json and writing it to file.

### Packages

| Name | Summary |
|---|---|
| [exercism.kotlin.autotests.executor](exercism.kotlin.autotests.executor/index.md) |  |
| [exercism.kotlin.autotests.runner](exercism.kotlin.autotests.runner/index.md) |  |
| [exercism.kotlin.autotests.runner.args](exercism.kotlin.autotests.runner.args/index.md) |  |
| [exercism.kotlin.autotests.runner.report](exercism.kotlin.autotests.runner.report/index.md) |  |
| [utils](utils/index.md) |  |
| [utils.junit](utils.junit/index.md) |  |

### Index

[All Types](alltypes/index.md)