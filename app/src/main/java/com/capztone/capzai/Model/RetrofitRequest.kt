package com.capstone.voicepix.Model

import retrofit2.http.Field
import retrofit2.http.Header

data class TextToImageRequest(
    @Field("text") val text: String,
    @Header("api-key") val apiKey: String
)
