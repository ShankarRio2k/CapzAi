package com.capstone.voicepix.TextToImage

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.voicepix.Model.TextToImageResponse
import com.capstone.voicepix.Retrofit.ApiService
import com.capstone.voicepix.Retrofit.RetrofitClient
import com.capztone.capzai.ApiCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextToImageViewmodel : ViewModel() {
    private val _response = MutableLiveData<TextToImageResponse>()
    private var callback: ApiCallBack? = null

    val response: LiveData<TextToImageResponse>
        get() = _response

    private val apiKey = "ebc8659c-7b7c-4836-9e2f-5b8a10a48116"

    fun setCallback(apiCallback: ApiCallBack?) {
        callback = apiCallback
    }

    fun getTextToImage(text: String) {
        val call = RetrofitClient.apiService.getTextToImage(text, apiKey)
        call.enqueue(object : Callback<TextToImageResponse> {
            override fun onResponse(call: Call<TextToImageResponse>, response: Response<TextToImageResponse>) {
                if (response.isSuccessful) {
                    val textToImageResponse = response.body()
                    textToImageResponse?.let {
                        _response.value = it
                    }
                } else {
                    val errorMessage = "API request failed: ${response.code()}"
                    callback?.onApiFailure(errorMessage)
                }
            }

            override fun onFailure(call: Call<TextToImageResponse>, t: Throwable) {
                val errorMessage = "Network request failed: ${t.message}"
                callback?.onApiFailure(errorMessage)
            }
        })
    }

}
