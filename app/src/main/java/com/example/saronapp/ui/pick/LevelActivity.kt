package com.example.saronapp.ui.pick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.saronapp.R
import com.example.saronapp.databinding.ActivityLevelBinding
import com.example.saronapp.ui.play.PlayActivity
import com.example.saronapp.utils.Constants.LEVEL_1
import com.example.saronapp.utils.Constants.LEVEL_2
import com.example.saronapp.utils.Constants.LEVEL_3
import com.example.saronapp.utils.Constants.LEVEL_4
import com.example.saronapp.utils.Constants.LEVEL_5
import com.example.saronapp.utils.Constants.PELOG
import com.example.saronapp.utils.Constants.SLENDRO

class LevelActivity : AppCompatActivity() {
    private var _binding: ActivityLevelBinding? = null
    private val binding get() = _binding

    private var type: Int? = null
    private var levelPassed: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        type = intent?.getIntExtra(EXTRA_TYPE, 1)

        if (type != null) {
            binding?.tvLevelTitle?.text = getString(R.string.level)
        }

        when (type) {
            PELOG -> {
                val myPrefs = getSharedPreferences(getString(R.string.prefs_pelog), MODE_PRIVATE)
                levelPassed = myPrefs.getString(getString(R.string.prefs_pelog), "0").toString()
            }
            SLENDRO -> {
                val myPrefs = getSharedPreferences(getString(R.string.prefs_slendro), MODE_PRIVATE)
                levelPassed = myPrefs.getString(getString(R.string.prefs_slendro), "0").toString()
            }
        }

        val intent = Intent(this, PlayActivity::class.java).also {
            it.putExtra(PlayActivity.EXTRA_TYPE, type)
        }

        binding?.btnLevel2?.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding?.btnLevel3?.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding?.btnLevel4?.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding?.btnLevel5?.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))

        binding?.btnLevel1?.setOnClickListener {
            intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_1)
            startActivity(intent)
        }
        if ((levelPassed.toInt() + 1) >= 2){
            binding?.btnLevel2?.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            binding?.btnLevel2?.setOnClickListener {
                intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_2)
                startActivity(intent)
            }
        }
        if ((levelPassed.toInt() + 1) >= 3){
            binding?.btnLevel3?.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            binding?.btnLevel3?.setOnClickListener {
                intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_3)
                startActivity(intent)
            }
        }
        if ((levelPassed.toInt() + 1) >= 4){
            binding?.btnLevel4?.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            binding?.btnLevel4?.setOnClickListener {
                intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_4)
                startActivity(intent)
            }
        }
        if ((levelPassed.toInt() + 1) >= 5){
            binding?.btnLevel5?.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            binding?.btnLevel5?.setOnClickListener {
                intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_5)
                startActivity(intent)
            }
        }
        binding?.btnBack?.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
    }
}