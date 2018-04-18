package org.skynetsoftware.pathfinder.rest

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

fun errorResponse(message: String? = null): Response
{
    val myResponse = MyResponse(message)
    return Response.status(500).type(MediaType.APPLICATION_JSON).entity(myResponse).build()
}

fun successResponse(message: String? = null, data: Any? = null): Response
{
    val myResponse = MyResponse(message, data)
    return Response.status(200).type(MediaType.APPLICATION_JSON).entity(myResponse).build()
}

data class MyResponse(val message: String? = null, val data: Any? = null)