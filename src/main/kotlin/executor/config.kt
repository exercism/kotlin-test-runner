package executor

import java.io.File

interface RuntimeConfiguration {
    val workingDirRoot: File
    val sourcesDir: File
    val testsDir: File
    val templateDir: File

    val purgeExistingWorkingDirBefore: Boolean get() = false
    val keepWorkingDirAfter: Boolean get() = true
}
