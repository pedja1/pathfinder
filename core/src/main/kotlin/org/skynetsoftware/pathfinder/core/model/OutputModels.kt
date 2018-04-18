package org.skynetsoftware.pathfinder.core.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Point(var row: Int = 0, var col: Int = 0)

data class Path(var points: List<Point> = ArrayList())

data class Result(@JsonProperty("execution_time_in_ms") var executonTimeInMs: Long = 0,
                  var path: Path? = null)