package com.example.saronapp.recorder

import android.content.Context
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
import java.io.File
import java.util.*

class SaronRecorder(
    context: Context,
    onStateChangeListener: (RecorderState) -> Unit
) {
    private var fileName: String = UUID.randomUUID().toString()
    private var filePath: String = context.externalCacheDir?.absolutePath + "/$fileName.wav"
    private var waveRecorder: WaveRecorder = WaveRecorder(filePath)

    private var counter = -1

    init {
        waveRecorder.onStateChangeListener = onStateChangeListener
    }

    fun startRecording() {
        waveRecorder.startRecording()
        this.plusCounter()
    }

    fun stopRecordingForResult(result: (counter: Int, audio: File) -> Unit) {
        waveRecorder.stopRecording()
        result(counter, File(filePath))
    }

    fun getCounter(): Int {
        return counter
    }

    fun plusCounter() {
        counter++
    }
}