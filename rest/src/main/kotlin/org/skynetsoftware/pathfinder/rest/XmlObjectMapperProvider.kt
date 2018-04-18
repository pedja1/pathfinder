package org.skynetsoftware.pathfinder.rest

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.ws.rs.ext.Provider

@Provider
class XmlObjectMapperProvider : JacksonJaxbXMLProvider()
{
    init
    {
        val mapper = XmlMapper()
        mapper.registerModule(KotlinModule())
        setMapper(mapper)
    }
}