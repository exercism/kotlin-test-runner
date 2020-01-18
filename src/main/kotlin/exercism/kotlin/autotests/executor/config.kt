package exercism.kotlin.autotests.executor

import exercism.kotlin.autotests.runner.args.LaunchArguments
import java.io.File

interface RuntimeConfiguration {
    val workingDirRoot: File
    val sourcesDir: File
    val testsDir: File
    val templateDir: File

    val args: LaunchArguments

    val purgeExistingWorkingDirBefore: Boolean get() = false
    val keepWorkingDirAfter: Boolean get() = true
}
