package com.capztone.capzai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capstone.voicepix.TextToImage.TextToImageViewmodel
import com.capztone.capzai.ApiCallBack
import com.capztone.capzai.R
import com.capztone.capzai.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home), ApiCallBack {

    private lateinit var binding: FragmentHomeBinding
    private val textToImageViewModel: TextToImageViewmodel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // Set click listener for the Generate button
        binding.submitButton.setOnClickListener {
            // Call your API using ViewModel when Generate button is clicked
            val textToGenerate = "cars"
            textToImageViewModel.setCallback(this) // Set the callback
            textToImageViewModel.getTextToImage(textToGenerate)
        }

        textToImageViewModel.response.observe(viewLifecycleOwner) { response ->
            // Check if the response is not null and contains a valid image URL
            response?.imageUrl?.let { imageUrl ->
                // Use Glide to load the image into the ImageView
                Toast.makeText(requireContext(), "Generated", Toast.LENGTH_SHORT).show()

                Glide.with(this)  // Use 'this' if you are inside a Fragment, use 'Glide.with(context)' if you are inside an Activity
                    .load(imageUrl)
                    .into(binding.imageView)
            }
        }
    }

    // Implement the callback method from ApiCallback interface
    override fun onApiFailure(errorMessage: String) {
        // Handle the error message, e.g., show a Toast
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        // Remove the callback reference to avoid memory leaks
        textToImageViewModel.setCallback(null)
        super.onDestroyView()
    }
}
