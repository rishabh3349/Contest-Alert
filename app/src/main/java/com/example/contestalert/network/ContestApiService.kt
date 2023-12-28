package com.example.contestalert.network


import com.example.contestalert.model.ResponseWrapper
import retrofit2.http.GET



interface ContestApiService {
    @GET("contest.list")
    suspend fun getAlert(): ResponseWrapper
}