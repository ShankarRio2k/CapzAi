package com.capstone.voicepix.Retrofit

import com.capstone.voicepix.Model.TextToImageResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("text2img") // Specify the relative URL for the POST request
    @FormUrlEncoded
    fun getTextToImage(
        @Field("text") text: String, // Form field for the text
        @Header("api-key") apiKey: String // Header parameter for the API key
    ): Call<TextToImageResponse>
}

