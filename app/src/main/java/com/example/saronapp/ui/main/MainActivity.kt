package com.example.saronapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.saronapp.databinding.ActivityMainBinding
import com.example.saronapp.ui.other.AboutActivity
import com.example.saronapp.ui.other.HowToPlayActivity
import com.example.saronapp.ui.pick.TypeActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            btnStart.setOnClickListener {
                Intent(this@MainActivity, TypeActivity::class.java).also {
                    startActivity(it)
                }
            }
            btnHowToPlay.setOnClickListener {
                Intent(this@MainActivity, HowToPlayActivity::class.java).also {
                    startActivity(it)
                }
            }
            btnAbout.setOnClickListener {
                Intent(this@MainActivity, AboutActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}