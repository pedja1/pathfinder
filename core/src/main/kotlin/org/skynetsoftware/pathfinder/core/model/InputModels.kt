package org.skynetsoftware.pathfinder.core.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

data class Cell(var row: Int = 0, var col: Int = 0)

@JacksonXmlRootElement(localName = "map")
data class Map(@JacksonXmlElementWrapper(localName = "cells") @JsonProperty("cells")var cells: List<Cell> = ArrayList(), @JsonProperty("start-point") var startPoint: Cell = Cell(),
               @JsonProperty("end-point") var endPoint: Cell = Cell())