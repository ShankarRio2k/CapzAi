package com.capztone.capzai.fragments

import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.voicepix.TextToImage.TextToImageViewmodel
import com.capztone.capzai.ApiCallBack
import com.capztone.capzai.LoadBannerAds
import android.Manifest
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.provider.MediaStore
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.capztone.capzai.R
import com.capztone.capzai.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class HomeFragment : Fragment(R.layout.fragment_home), ApiCallBack {

    private lateinit var binding: FragmentHomeBinding
    private val textToImageViewModel: TextToImageViewmodel by viewModels()
    private var selectedPerformanceLevel: String = ""
    private lateinit var loadingDialog: Dialog
    private var selectedSubject: String = ""
    private lateinit var downloadDialog: Dialog
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val bannerAd = binding.adFrameLayout
        LoadBannerAds(requireContext(), bannerAd, AdSize.BANNER, R.string.banner_ad_unit_id)

        setupLoadingDialog()
        setupDownloadDialog()

        val grades = Array(12) { "Grade ${it + 1}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownSpinner.adapter = adapter

        val subjects = arrayOf("Science", "Maths", "Physics", "Chemistry")
        val subjectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.subjectSpinner.adapter = subjectAdapter

        binding.dropdownSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGrade = parent.getItemAtPosition(position) as String
                selectedPerformanceLevel = getPerformanceLevel(selectedGrade)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedPerformanceLevel = ""
            }
        }

        binding.subjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSubject = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedSubject = ""
            }
        }

        binding.submitButton.setOnClickListener {
            generateImage()
        }

        // Check if there's a prompt text passed from IdeaFragment
        arguments?.getString("promptText")?.let {
            binding.multilineInput.setText(it)
            binding.submitButton.performClick() // Trigger the generate button click
        }

        textToImageViewModel.response.observe(viewLifecycleOwner) { response ->
            loadingDialog.dismiss()
            if (response == null) {
                Toast.makeText(requireContext(), "Failed to generate image. Please try again later.", Toast.LENGTH_SHORT).show()
            } else {
                response.imageUrl?.let { imageUrl ->
                    Toast.makeText(requireContext(), "Image generated successfully", Toast.LENGTH_SHORT).show()
                    Glide.with(this)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.idea1)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.imageView)
                } ?: run {
                    Toast.makeText(requireContext(), "Failed to generate image. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.downloadImage.setOnClickListener {
            val imageUrl = textToImageViewModel.response.value?.imageUrl
            imageUrl?.let { url ->
                downloadImage(url)
            }
        }
    }

    private fun generateImage() {
        val inputText = binding.multilineInput.text.toString()
        if (inputText.isBlank()) {
            Toast.makeText(requireContext(), "Please enter some text", Toast.LENGTH_SHORT).show()
            return
        }

        loadingDialog.show()

        val textToGenerate = "$selectedPerformanceLevel $selectedSubject $inputText"

        val sharedPreferences = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val widthHeight = sharedPreferences.getString("width_height", "512 x 512")
        val (width, height) = widthHeight?.split(" x ")?.map { it.trim() } ?: listOf("512", "512")
        val gridSize = sharedPreferences.getString("grid_size", "1") ?: "1"
        val imageGeneratorVersion = sharedPreferences.getString("image_version", "hd") ?: "hd"
        val apiKey = "ebc8659c-7b7c-4836-9e2f-5b8a10a48116"

        textToImageViewModel.setCallback(this)
        textToImageViewModel.getTextToImage(
            text = textToGenerate,
            gridSize = gridSize,
            width = width,
            height = height,
            imageGeneratorVersion = imageGeneratorVersion,
            negativePrompt = "",
            apiKey = apiKey
        )

        textToImageViewModel.response.observe(viewLifecycleOwner) { response ->
            loadingDialog.dismiss()
            if (response == null) {
                Toast.makeText(requireContext(), "Failed to generate image. Please try again later.", Toast.LENGTH_SHORT).show()
            } else {
                response.imageUrl?.let { imageUrl ->
                    Toast.makeText(requireContext(), "Image generated successfully", Toast.LENGTH_SHORT).show()
                    Glide.with(this)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.idea1)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.imageView)
                } ?: run {
                    Toast.makeText(requireContext(), "Failed to generate image. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.downloadImage.setOnClickListener {
            val imageUrl = textToImageViewModel.response.value?.imageUrl
            imageUrl?.let { url ->
                downloadImage(url)
            }
        }
    }

    private fun setupLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog.setContentView(R.layout.lottie_loading_screen)
        loadingDialog.setCancelable(false)
        loadingDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun setupDownloadDialog() {
        downloadDialog = Dialog(requireContext())
        downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        downloadDialog.setContentView(R.layout.dialog_download_progress)
        downloadDialog.setCancelable(false)
        downloadDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        downloadDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressBar = downloadDialog.findViewById(R.id.progressBar)
        progressText = downloadDialog.findViewById(R.id.tvProgressPercentage)
    }

    private fun downloadImage(url: String) {
        downloadDialog.show()
        progressBar.progress = 0
        progressText.text = "0%"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                val totalSize = urlConnection.contentLength
                val inputStream = BufferedInputStream(urlConnection.inputStream)
                val outputFile = File(requireContext().cacheDir, "downloaded_image.jpg")
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(1024)
                var downloadedSize = 0
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    downloadedSize += bytesRead
                    val progress = (downloadedSize * 100 / totalSize)
                    withContext(Dispatchers.Main) {
                        progressBar.progress = progress
                        progressText.text = "$progress%"
                    }
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                saveBitmapToGallery(BitmapFactory.decodeFile(outputFile.path))
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Image saved to Gallery", Toast.LENGTH_SHORT).show()
                    downloadDialog.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
                    downloadDialog.dismiss()
                }
            }
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (imageUri != null) {
            try {
                val outputStream = resolver.openOutputStream(imageUri)
                outputStream.use { out ->
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                }
                MediaScannerConnection.scanFile(
                    requireContext(),
                    arrayOf(imageUri.toString()),
                    null,
                    null
                )
            } catch (e: IOException) {
                e.printStackTrace()
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

    override fun onApiFailure(errorMessage: String) {
        loadingDialog.dismiss()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        textToImageViewModel.setCallback(null)
        super.onDestroyView()
    }
}
