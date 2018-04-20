package org.skynetsoftware.pathfinder.net

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * [RestApi] implementation for pathfinder API*/
class PathfinderRestApi : RestApi<PathfinderService> {
    override val service: PathfinderService
    override val retrofit: Retrofit

    init {
        val httpClient = OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()

        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        retrofit = Retrofit.Builder()
                .baseUrl("http://skynetsoftware.org:8088")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(httpClient)
                .build()
        service = retrofit.create(PathfinderService::class.java)
    }
}
