package com.example.saronapp.ui.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.saronapp.databinding.ActivityHowToPlayBinding

class HowToPlayActivity : AppCompatActivity() {
    private var _binding: ActivityHowToPlayBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHowToPlayBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Button Listener
        binding?.btnBack?.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}