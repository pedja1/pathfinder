package org.skynetsoftware.pathfinder.core.model

data class Node(var parent: Node?, val row: Int, val col: Int, var G: Int = 0, var H: Int = -1) {
    val F: Int
        get() {
            return G + H
        }

    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Node)
            return false
        val node2: Node = other
        return row == node2.row && col == node2.col
    }
}