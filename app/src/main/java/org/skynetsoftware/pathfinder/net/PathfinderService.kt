package org.skynetsoftware.pathfinder.net

import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PathfinderService {
    @POST("/pathfinder/json")
    @Headers("Accept: application/xml")
    fun solvePath(@Body map: Map): Call<Result>
}