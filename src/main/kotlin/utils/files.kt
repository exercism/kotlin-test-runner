package utils

import java.io.File

/** Take this string and resolve it as a path to [File] relatively to current directory of running process */
fun String.resolveAsDir(): File = File(".").resolve(this)
