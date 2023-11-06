rootProject.name = "kotlin-test-runner"

include("lib")
include(
    "examples:full",
    "examples:template",
)
include(
    "tests:example-all-fail-junit4",
    "tests:example-all-fail-junit5",
    "tests:example-empty-file-junit4",
    "tests:example-empty-file-junit5",
    "tests:example-partial-fail-junit4",
    "tests:example-partial-fail-junit5",
    "tests:example-success-junit4",
    "tests:example-success-junit5",
    "tests:example-syntax-error-junit4",
    "tests:example-syntax-error-junit5",
)
