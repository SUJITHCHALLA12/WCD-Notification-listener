package com.pentester.wcd.ui.developer

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.pentester.wcd.databinding.ActivityDeveloperInfoBinding

class DeveloperInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeveloperInfoBinding
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.developerImage.setImageURI(uri)
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_PROFILE_URI, uri.toString())
                .apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeveloperInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedUri = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getString(KEY_PROFILE_URI, null)
        if (!savedUri.isNullOrBlank()) {
            binding.developerImage.setImageURI(Uri.parse(savedUri))
        }

        binding.developerImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    companion object {
        private const val PREFS_NAME = "developer_info"
        private const val KEY_PROFILE_URI = "profile_uri"
    }
}
