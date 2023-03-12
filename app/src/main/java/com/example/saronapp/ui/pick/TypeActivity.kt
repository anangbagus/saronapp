package com.example.saronapp.ui.pick

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.saronapp.databinding.ActivityTypeBinding
import com.example.saronapp.utils.Constants.PELOG
import com.example.saronapp.utils.Constants.SLENDRO

class TypeActivity : AppCompatActivity() {
    private var _binding: ActivityTypeBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            btnPelogType.setOnClickListener {
                Intent(this@TypeActivity, LevelActivity::class.java).also {
                    it.putExtra(LevelActivity.EXTRA_TYPE, PELOG)
                    startActivity(it)
                }
            }
            btnSlendroType.setOnClickListener {
                Intent(this@TypeActivity, LevelActivity::class.java).also {
                    it.putExtra(LevelActivity.EXTRA_TYPE, SLENDRO)
                    startActivity(it)
                }
            }
            btnBack.setOnClickListener { finish() }
        }
    }
}