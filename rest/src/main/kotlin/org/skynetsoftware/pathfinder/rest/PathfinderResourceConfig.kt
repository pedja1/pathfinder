package org.skynetsoftware.pathfinder.rest

import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig

class PathfinderResourceConfig: ResourceConfig()
{
    init
    {
        register(PathfinderResource::class.java)
        register(GlobalExceptionMapper::class.java)
        register(JacksonFeature::class.java)
        register(BufferedImageBodyWriter::class.java)
    }
}