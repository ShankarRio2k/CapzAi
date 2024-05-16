package com.capstone.voicepix.Model

import com.google.gson.annotations.SerializedName

data class TextToImageResponse(
    @SerializedName("id") val id: String,
    @SerializedName("output_url") val imageUrl: String // Assuming "url" is a field in the JSON response
)
