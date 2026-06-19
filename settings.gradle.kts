rootProject.name = "kotlin-test-runner"

include("lib")
include(
    "examples:full",
    "examples:template",
)
include(
    "tests:example-all-fail-junit4",
    "tests:example-all-fail-junit6",
    "tests:example-empty-file-junit4",
    "tests:example-empty-file-junit6",
    "tests:example-partial-fail-junit4",
    "tests:example-partial-fail-junit6",
    "tests:example-success-junit4",
    "tests:example-success-junit6",
    "tests:example-syntax-error-junit4",
    "tests:example-syntax-error-junit6",
)
