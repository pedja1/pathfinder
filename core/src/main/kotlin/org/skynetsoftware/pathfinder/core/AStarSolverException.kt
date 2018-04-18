package org.skynetsoftware.pathfinder.core

class AStarSolverException : Exception
{
    constructor(message: String, cause: Throwable): super(message, cause)
    constructor(message: String): super(message)
    constructor(): super()
}