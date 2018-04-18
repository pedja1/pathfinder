package org.skynetsoftware.pathfinder.core.model

data class Point(var row: Int = 0, var col: Int = 0)

data class Path(var points: List<Point> = ArrayList())

data class Result(var executonTimeInMs: Long = 0,
                  var path: Path? = null)