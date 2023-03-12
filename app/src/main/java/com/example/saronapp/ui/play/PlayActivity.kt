package com.example.saronapp.ui.play

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.saronapp.R
import com.example.saronapp.databinding.ActivityPlayBinding
import com.example.saronapp.data.media.AudioPlayer
import com.example.saronapp.recorder.SaronRecorder
import com.example.saronapp.ui.pick.TypeActivity
import com.example.saronapp.ui.score.ScoreFragment
import com.example.saronapp.utils.BilahImageView
import com.example.saronapp.utils.Constants.PELOG
import com.example.saronapp.utils.Constants.SLENDRO
import com.example.saronapp.utils.Constants.LEVEL_1
import com.example.saronapp.utils.Constants.LEVEL_2
import com.example.saronapp.utils.Constants.LEVEL_3
import com.example.saronapp.utils.Constants.LEVEL_4
import com.example.saronapp.utils.Constants.LEVEL_5
import com.example.saronapp.utils.Pattern
import com.example.saronapp.utils.Status
import com.example.saronapp.viewmodel.MainViewModel
import com.github.squti.androidwaverecorder.RecorderState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

private const val PERMISSION_REQUEST_RECORD_AUDIO = 77
private const val REQUEST_CODE = 1

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PlayActivity : AppCompatActivity() {
    private var _binding: ActivityPlayBinding? = null
    private val binding get() = _binding

    private val viewModel: MainViewModel by viewModels()

    private var type: Int? = null
    private var typeStr: String? = ""
    private var level: Int? = null
    private var pattern = arrayListOf<Int>()
    private var currBilah: String? = ""

    private var isRecording = false
    private lateinit var uri: Uri

    private lateinit var saronRecorder: SaronRecorder

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        type = intent?.getIntExtra(EXTRA_TYPE, 0)
        level = intent?.getIntExtra(EXTRA_LEVEL, 0)?.minus(1)

        when (type) {
            PELOG -> {
                pattern = Pattern.PATTERN_PELOG[level!!]
                typeStr = "pelog"
            }
            SLENDRO -> {
                pattern = Pattern.PATTERN_SLENDRO[level!!]
                typeStr = "slendro"
            }
        }
        currBilah = typeStr + pattern[0].toString()

        when (level!! + 1) {
            LEVEL_1 -> {
                binding?.tvLevelTitle?.text = getString(R.string.title_level_1)
            }
            LEVEL_2 -> {
                binding?.tvLevelTitle?.text = getString(R.string.title_level_2)
            }
            LEVEL_3 -> {
                binding?.tvLevelTitle?.text = getString(R.string.title_level_3)
            }
            LEVEL_4 -> {
                binding?.tvLevelTitle?.text = getString(R.string.title_level_4)
            }
            LEVEL_5 -> {
                binding?.tvLevelTitle?.text = getString(R.string.title_level_5)
            }
        }

        when (type) {
            PELOG -> {
                binding?.tvType?.text = getString(R.string.pelog_type)
                val bilahImageView: BilahImageView =
                    binding?.saronContainer?.getChildAt(Pattern.PATTERN_PELOG[level!!][0] - 1) as BilahImageView
                bilahImageView.highlight()
            }
            SLENDRO -> {
                binding?.tvType?.text = getString(R.string.slendro_type)
                binding?.numberContainer?.getChildAt(6)?.visibility = View.GONE
                binding?.saronContainer?.getChildAt(6)?.visibility = View.GONE
                val bilahImageView: BilahImageView =
                    binding?.saronContainer?.getChildAt(Pattern.PATTERN_SLENDRO[level!!][0] - 1) as BilahImageView
                bilahImageView.highlight()
            }
        }

        saronRecorder = SaronRecorder(this) {
            when (it) {
                RecorderState.RECORDING -> startRecordingState()
                RecorderState.STOP -> stopRecordingState()
                else -> stopRecordingState()
            }
        }

        val progressDialog = AlertDialog.Builder(this)
            .setView(R.layout.layout_progress_bar)
            .setCancelable(false)
            .create()

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressDialog.show()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    Intent(this, TypeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                .create()
                .show()
        }

        viewModel.currentPrediction.observe(this) {
            Log.d("observeStatus", "${it.status}")
            if (it.status == Status.SUCCESS) {
                viewModel.setCurrentPrediction()
                progressDialog.dismiss()
                if (saronRecorder.getCounter() == pattern.size - 1) {
                    val bundle = Bundle()
                    val score = viewModel.score.value!! * (100.toFloat() / pattern.size)
                    if (score >= 80) {
                        when (type) {
                            PELOG -> {
                                val myPrefs = getSharedPreferences(getString(R.string.prefs_pelog), MODE_PRIVATE)
                                val levelpassed = myPrefs.getString(getString(R.string.prefs_pelog), "0")
                                if (level == levelpassed?.toInt()) {
                                    myPrefs.edit().putString(getString(R.string.prefs_pelog), (level!! + 1).toString()).apply()
                                }
                            }
                            SLENDRO -> {
                                val myPrefs = getSharedPreferences(getString(R.string.prefs_slendro), MODE_PRIVATE)
                                val levelpassed = myPrefs.getString(getString(R.string.prefs_slendro), "0")
                                if (level == levelpassed?.toInt()) {
                                    myPrefs.edit().putString(getString(R.string.prefs_slendro), (level!! + 1).toString()).apply()
                                }
                            }
                        }
                    }
                    bundle.putFloat(ScoreFragment.EXTRA_SCORE, score)
                    val dialog = ScoreFragment()
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, ScoreFragment::class.java.simpleName)
                }
            }
            if (it.status == Status.ERROR) {
                viewModel.setCurrentPrediction()
                progressDialog.dismiss()
            }
        }

        binding?.apply {
            btnStartStop.setOnClickListener {
                audioPlayer.stopAudioFile()
                if (!isRecording) {
                    if (ContextCompat.checkSelfPermission(
                            this@PlayActivity,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@PlayActivity,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            PERMISSION_REQUEST_RECORD_AUDIO
                        )
                    } else {
                        saronRecorder.startRecording()
                    }
                } else {
                    saronRecorder.stopRecordingForResult { counter, audio ->
                        Log.d("scoringRekam", "$audio")
                        scoring(counter, audio)
                    }
                }
            }
            btnPlay.setOnClickListener {
                if (!isRecording){
                    val resName = currBilah
                    val resId = resources.getIdentifier(resName, "raw", packageName)
                    audioPlayer.playAudioFile(this@PlayActivity, resId)
                }
            }
            btnTesting.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(Intent.createChooser(intent, "Pilih file"), REQUEST_CODE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startRecordingState() {
        isRecording = true
        binding?.apply {
            tvInfo.text = getString(R.string.recording)
            btnStartStop.text = getString(R.string.stop)
        }
    }

    private fun stopRecordingState() {
        isRecording = false
        binding?.apply {
            tvInfo.text = getString(R.string.stopped)
            btnStartStop.text = getString(R.string.record)
        }
    }

    private fun scoring(counter: Int, audio: File) {
        if (counter == pattern.size - 1) {
            val bilah = pattern[counter]
            var expectedBilah = String()

            when (type) {
                PELOG -> {
                    expectedBilah = "pelog$bilah"
                }
                SLENDRO -> {
                    expectedBilah = "slendro$bilah"
                }
            }

            val expectedBilahPart: RequestBody = expectedBilah.toRequestBody(MultipartBody.FORM)
            val audioRequestBody = audio.asRequestBody()
            val audioPart = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audioRequestBody
            )
            viewModel.postRequest(expectedBilahPart, audioPart)
            return
        }

        if (counter < pattern.size) {
            val prevIndex = pattern[counter] - 1
            val prevBilahView =
                binding?.saronContainer?.getChildAt(prevIndex) as BilahImageView
            prevBilahView.removeHighlight()

            if (counter != pattern.size - 1) {
                currBilah = typeStr + pattern[counter + 1]
                val nextIndex = pattern[counter + 1] - 1
                val nextBilahView =
                    binding?.saronContainer?.getChildAt(nextIndex) as BilahImageView
                nextBilahView.highlight()
            }

            val bilah = pattern[counter]
            var expectedBilah = String()
            when (type) {
                PELOG -> {
                    expectedBilah = "pelog$bilah"
                }
                SLENDRO -> {
                    expectedBilah = "slendro$bilah"
                }
            }

            val expectedBilahPart: RequestBody = expectedBilah.toRequestBody(MultipartBody.FORM)
            val audioRequestBody = audio.asRequestBody()
            val audioPart = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audioRequestBody
            )
            viewModel.postRequest(expectedBilahPart, audioPart)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }

            uri = data.data!!
            saronRecorder.plusCounter()
            val counter = saronRecorder.getCounter()

            val inputStream = this.contentResolver.openInputStream(uri)
            val file = File(this.cacheDir, "temp_audio_file")

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }

            if (counter == pattern.size - 1) {
                val bilah = pattern[counter]
                var expectedBilah = String()

                when (type) {
                    PELOG -> {
                        expectedBilah = "pelog$bilah"
                    }
                    SLENDRO -> {
                        expectedBilah = "slendro$bilah"
                    }
                }

                val expectedBilahPart: RequestBody = expectedBilah.toRequestBody(MultipartBody.FORM)
                val audioRequestBody = file.asRequestBody()
                val audioPart = MultipartBody.Part.createFormData(
                    "audio",
                    file.name,
                    audioRequestBody
                )
                viewModel.postRequest(expectedBilahPart, audioPart)
                return
            }

            if (counter < pattern.size) {
                val prevIndex = pattern[counter] - 1
                val prevBilahView =
                    binding?.saronContainer?.getChildAt(prevIndex) as BilahImageView
                prevBilahView.removeHighlight()

                if (counter != pattern.size - 1) {
                    currBilah = typeStr + pattern[counter + 1]
                    val nextIndex = pattern[counter + 1] - 1
                    val nextBilahView =
                        binding?.saronContainer?.getChildAt(nextIndex) as BilahImageView
                    nextBilahView.highlight()
                }

                val bilah = pattern[counter]
                var expectedBilah = String()
                when (type) {
                    PELOG -> {
                        expectedBilah = "pelog$bilah"
                    }
                    SLENDRO -> {
                        expectedBilah = "slendro$bilah"
                    }
                }

                val expectedBilahPart: RequestBody = expectedBilah.toRequestBody(MultipartBody.FORM)
                val audioRequestBody = file.asRequestBody()
                val audioPart = MultipartBody.Part.createFormData(
                    "audio",
                    file.name,
                    audioRequestBody
                )
                viewModel.postRequest(expectedBilahPart, audioPart)
            }
        }
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_LEVEL = "extra_level"
    }
}