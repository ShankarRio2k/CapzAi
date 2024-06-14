package com.capztone.capzai.fragments

import com.capztone.capzai.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SettingsFragment : Fragment() {

    private lateinit var widthHeightSpinner: Spinner
    private lateinit var gridSizeSpinner: Spinner
    private lateinit var negativePromptChipGroup: ChipGroup
    private lateinit var imageVersionSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize views
        widthHeightSpinner = view.findViewById(R.id.widthHeightSpinner)
        gridSizeSpinner = view.findViewById(R.id.gridSizeSpinner)
        negativePromptChipGroup = view.findViewById(R.id.negativePromptChipGroup)
        imageVersionSpinner = view.findViewById(R.id.version)
        saveButton = view.findViewById(R.id.submit_button)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Load saved settings
        loadSettings()

        // Set save button click listener
        saveButton.setOnClickListener {
            saveSettings()
        }

        // Set spinner item selected listeners to save immediately
        widthHeightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedWidthHeight = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("width_height", selectedWidthHeight)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        gridSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedGridSize = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("grid_size", selectedGridSize)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageVersionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedImageVersion = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("image_version", selectedImageVersion)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        return view
    }

    private fun loadSettings() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Load saved width and height
        val widthHeight = sharedPreferences.getString("width_height", "512 x 512")
        val widthHeightAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.width_height_values,
            android.R.layout.simple_spinner_item
        )
        widthHeightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        widthHeightSpinner.adapter = widthHeightAdapter

        val widthHeightPosition = widthHeightAdapter.getPosition(widthHeight)
        if (widthHeightPosition >= 0) {
            widthHeightSpinner.setSelection(widthHeightPosition)
        }

        // Set spinner item selected listeners to save immediately
        widthHeightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedWidthHeight = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("width_height", selectedWidthHeight)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Load saved grid size
        val gridSize = sharedPreferences.getString("grid_size", "1")
        val gridSizeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.grid_size_values,
            android.R.layout.simple_spinner_item
        )
        gridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gridSizeSpinner.adapter = gridSizeAdapter

        val gridSizePosition = gridSizeAdapter.getPosition(gridSize)
        if (gridSizePosition >= 0) {
            gridSizeSpinner.setSelection(gridSizePosition)
        }

        gridSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedGridSize = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("grid_size", selectedGridSize)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Load saved negative prompts
        val negativePrompt = sharedPreferences.getString("negative_prompt", "")
        for (i in 0 until negativePromptChipGroup.childCount) {
            val chip = negativePromptChipGroup.getChildAt(i) as Chip
            chip.isChecked = chip.text.toString() == negativePrompt
        }

        // Load saved image generation version
        val imageVersion = sharedPreferences.getString("image_version", "HD")
        val imageVersionAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.version_values,
            android.R.layout.simple_spinner_item
        )
        imageVersionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        imageVersionSpinner.adapter = imageVersionAdapter

        val imageVersionPosition = imageVersionAdapter.getPosition(imageVersion)
        if (imageVersionPosition >= 0) {
            imageVersionSpinner.setSelection(imageVersionPosition)
        }

        imageVersionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedImageVersion = parent.getItemAtPosition(position).toString()
                with(sharedPreferences.edit()) {
                    putString("image_version", selectedImageVersion)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun saveSettings() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Save width and height
        val widthHeight = widthHeightSpinner.selectedItem.toString()
        sharedPreferences.edit().putString("width_height", widthHeight).apply()

        // Save grid size
        val gridSize = gridSizeSpinner.selectedItem.toString()
        sharedPreferences.edit().putString("grid_size", gridSize).apply()

        // Save negative prompt
        var negativePrompt = ""
        for (i in 0 until negativePromptChipGroup.childCount) {
            val chip = negativePromptChipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                negativePrompt = chip.text.toString()
                break
            }
        }
        sharedPreferences.edit().putString("negative_prompt", negativePrompt).apply()

        // Save image generation version
        val imageVersion = imageVersionSpinner.selectedItem.toString()
        sharedPreferences.edit().putString("image_version", imageVersion).apply()

        Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show()
    }
}
