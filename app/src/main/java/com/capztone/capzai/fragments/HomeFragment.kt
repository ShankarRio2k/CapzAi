package com.capztone.capzai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capztone.capzai.R
import com.capztone.capzai.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the GIF into the ImageView using Glide
//        Glide.with(this)
//            .asGif()
//            .load(R.drawable.your_gif) // Replace with your GIF resource
//            .into(binding.imageView)

        // Setup other view elements if needed
        binding.submitButton.setOnClickListener {
            // Handle button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}