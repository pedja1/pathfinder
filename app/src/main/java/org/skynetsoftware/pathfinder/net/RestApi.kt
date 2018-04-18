package org.skynetsoftware.pathfinder.net

import retrofit2.Retrofit

/**
 * Created by pedja on 2/23/18.
 */

interface RestApi<T> {
    val service: T
    val retrofit: Retrofit
}
