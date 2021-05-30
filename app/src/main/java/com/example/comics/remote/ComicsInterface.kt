package com.example.comics.remote

import com.example.comics.models.ComicsInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComicsInterface {

    @GET("{id}/info.0.json")
    suspend fun getComics(@Path("id") id : Int ): Response<ComicsInfo>
}