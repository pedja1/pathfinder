package org.skynetsoftware.pathfinder.net

import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PathfinderService {
    @POST("/pathfinder/json")
    fun findPath(@Body map: Map): Call<Result>
}