package org.skynetsoftware.pathfinder.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class GlobalExceptionMapper : ExceptionMapper<Throwable>
{
    override fun toResponse(exception: Throwable?): Response
    {
        return errorResponse(exception?.message)
    }
}