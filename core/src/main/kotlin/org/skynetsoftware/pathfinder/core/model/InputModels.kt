package org.skynetsoftware.pathfinder.core.model

data class Cell(var row: Int = 0, var col: Int = 0)

data class Map(var cells: List<Cell> = ArrayList(), var startPoint: Cell = Cell(),
               var endPoint: Cell = Cell())