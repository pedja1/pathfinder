package org.skynetsoftware.pathfinder.core.utils

class Timer {
    private var start: Long = 0

    fun start() {
        this.start = System.currentTimeMillis()
    }

    fun end(): Long {
        val now = System.currentTimeMillis()
        return now - start
    }
}