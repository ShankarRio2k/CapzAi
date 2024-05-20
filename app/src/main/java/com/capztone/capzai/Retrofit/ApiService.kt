package com.capstone.voicepix.Retrofit

import com.capstone.voicepix.Model.TextToImageResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("text2img")
    @FormUrlEncoded
    fun getTextToImage(
        @Field("text") text: String,
        @Field("grid_size") gridSize: String = "2",
        @Field("width") width: String = "512",
        @Field("height") height: String = "512",
        @Field("image_generator_version") imageGeneratorVersion: String = "hd",
        @Field("negative_prompt") negativePrompt: String = "blurry and pixelated unclear",
        @Header("api-key") apiKey: String
    ): Call<TextToImageResponse>
}

