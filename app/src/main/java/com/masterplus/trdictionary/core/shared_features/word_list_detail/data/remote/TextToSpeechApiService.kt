package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TextToSpeechApiService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("v1/text:synthesize")
    suspend fun synthesize(
        @Header("Authorization") authorization: String,
        @Header("x-android-package") packageName: String,
        @Header("x-android-cert") cert: String,
        @Query("key") key: String,
        @Body request: TextToSpeechRequest,
    ): Response<TextToSpeechResponse>
}