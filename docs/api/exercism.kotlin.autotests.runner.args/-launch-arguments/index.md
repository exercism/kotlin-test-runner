[api](../../index.md) / [exercism.kotlin.autotests.runner.args](../index.md) / [LaunchArguments](./index.md)

# LaunchArguments

`data class LaunchArguments`

Data structure to keep resolved launch arguments

### Constructors

| [&lt;init&gt;](-init-.md) | Data structure to keep resolved launch arguments`LaunchArguments(exerciseSlug: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, solutionsDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, resultFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`)` |

### Properties

| [exerciseSlug](exercise-slug.md) | Exercise slug (e.g. `hello-world`). Currently ignored.`val exerciseSlug: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [resultFile](result-file.md) | [File](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) pointing to resolved `results.json` file in output directory.`val resultFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [solutionsDir](solutions-dir.md) | [File](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) pointing to solutions directory.`val solutionsDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |

### Companion Object Extension Functions

| [parseAndValidate](../parse-and-validate.md) | Parse launch arguments and validate them. [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) can be thrown if some arguments are invalid or not provided.`fun LaunchArguments.Companion.parseAndValidate(arguments: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`LaunchArguments`](./index.md) |

