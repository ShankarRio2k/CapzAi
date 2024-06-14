package com.capstone.voicepix.Retrofit

import com.capstone.voicepix.Model.TextToImageResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("text2img")
    @FormUrlEncoded
    fun getTextToImage(
        @Field("text") text: String,
        @Field("grid_size") gridSize: String,
        @Field("width") width: String,
        @Field("height") height: String,
        @Field("image_generator_version") imageGeneratorVersion: String,
        @Field("negative_prompt") negativePrompt: String,
        @Header("api-key") apiKey: String
    ): Call<TextToImageResponse>
}

