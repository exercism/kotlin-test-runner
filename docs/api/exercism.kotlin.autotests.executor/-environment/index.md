[api](../../index.md) / [exercism.kotlin.autotests.executor](../index.md) / [Environment](./index.md)

# Environment

`data class Environment`

### Constructors

| [&lt;init&gt;](-init-.md) | `Environment(workingDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, resultFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`)` |

### Properties

| [resultFile](result-file.md) | Reference to `results.json` [File](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) that suppose to be written in the end of testing process.`val resultFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [workingDir](working-dir.md) | Temporary working directory that is copy of solutions directory but can be safely modified.`val workingDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |

