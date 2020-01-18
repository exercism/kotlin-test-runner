package utils

import java.io.File

fun String.resolveAsDir(): File = File(".").resolve(this)
