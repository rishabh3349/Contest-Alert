package com.example.contestalert.data

import com.example.contestalert.network.ContestApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val contestAlertRepository: ContestAlertRepository
}
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://codeforces.com/api/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: ContestApiService by lazy {
        retrofit.create(ContestApiService::class.java)
    }
    override val contestAlertRepository: ContestAlertRepository by lazy {
        DefaultContestAlertRepository(retrofitService)
    }
}