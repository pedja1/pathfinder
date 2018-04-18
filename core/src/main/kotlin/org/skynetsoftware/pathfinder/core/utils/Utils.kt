package org.skynetsoftware.pathfinder.core.utils

import java.io.File

fun assertFileReadable(file: File)
{
    if (!file.exists() || !file.canRead())
        throw IllegalStateException(String.format("Can't read file '%s'", file.absoluteFile))
}

fun isFileReadable(file: File): Boolean
{
    return file.exists() && file.canRead()
}