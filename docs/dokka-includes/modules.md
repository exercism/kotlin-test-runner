# Module kotlin-test-runner

Test Runner has few stages:

1. parsing `LaunchArguments`;
2. preparing environment (e.g. creating temporary working directory, copying source files to working directory, cleaning up tests etc);
3. running `./gradlew test` inside working directory to execute tests;
4. parsing JUnit test results;
5. converting test results to [predefined report structure](https://github.com/exercism/automated-tests/blob/master/docs/interface.md);
6. marshaling report to json and writing it to file.
