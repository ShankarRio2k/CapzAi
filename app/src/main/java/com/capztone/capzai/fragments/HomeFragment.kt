package com.capztone.capzai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capstone.voicepix.TextToImage.TextToImageViewmodel
import com.capztone.capzai.ApiCallBack
import com.capztone.capzai.LoadBannerAds
import com.capztone.capzai.R
import com.capztone.capzai.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds


class HomeFragment : Fragment(R.layout.fragment_home), ApiCallBack {

    private lateinit var binding: FragmentHomeBinding
    private val textToImageViewModel: TextToImageViewmodel by viewModels()
    private var selectedPerformanceLevel: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val BannerAd = binding.adFrameLayout

        LoadBannerAds(requireContext(),BannerAd, AdSize.BANNER,R.string.banner_ad_unit_id)

        // Populate the spinner with grades 1 to 12
        val grades = Array(12) { "Grade ${it + 1}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownSpinner.adapter = adapter

        // Set spinner item selected listener to update performance input
        binding.dropdownSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGrade = parent.getItemAtPosition(position) as String
                selectedPerformanceLevel = getPerformanceLevel(selectedGrade)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedPerformanceLevel = ""
            }
        }

        // Set click listener for the Generate button
        binding.submitButton.setOnClickListener {
            // Get input from TextInputEditText
            val inputText = binding.multilineInput.text.toString()

            // Use both spinner and input text to form the request
            val textToGenerate = "$selectedPerformanceLevel $inputText"

            // Call your API using ViewModel when Generate button is clicked
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


    private fun getPerformanceLevel(grade: String): String {
        return when (grade) {
            "Grade 1" -> "Beginner level"
            "Grade 2" -> "Emerging level"
            "Grade 3" -> "Developing level"
            "Grade 4" -> "Approaching Proficient level"
            "Grade 5" -> "Proficient level"
            "Grade 6" -> "Advanced level"
            "Grade 7" -> "Highly Advanced level"
            "Grade 8" -> "Exceptional level"
            "Grade 9" -> "Outstanding level"
            "Grade 10" -> "Mastery level"
            "Grade 11" -> "Expert level"
            "Grade 12" -> "Virtuoso level"
            else -> ""
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
