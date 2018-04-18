package org.skynetsoftware.pathfinder.rest

import java.awt.image.BufferedImage
import java.io.IOException
import java.io.OutputStream
import java.lang.reflect.Type

import javax.imageio.ImageIO
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider

@Produces("image/png", "image/jpg")
@Provider
class BufferedImageBodyWriter : MessageBodyWriter<BufferedImage>
{
    override fun isWriteable(type: Class<*>, type1: Type, antns: Array<Annotation>, mt: MediaType): Boolean
    {
        return type == BufferedImage::class.java
    }

    override fun getSize(t: BufferedImage, type: Class<*>, type1: Type, antns: Array<Annotation>, mt: MediaType): Long
    {
        return -1 // not used in JAX-RS 2
    }

    @Throws(IOException::class, WebApplicationException::class)
    override fun writeTo(image: BufferedImage, type: Class<*>, type1: Type, antns: Array<Annotation>, mt: MediaType, mm: MultivaluedMap<String, Any>, out: OutputStream)
    {
        ImageIO.write(image, mt.subtype, out)
    }
}