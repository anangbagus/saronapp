package com.example.saronapp.data.media

import android.content.Context
import android.net.Uri

interface AudioPlayer {
    fun playAudioFile(context: Context, audioResId: Int)
    fun playAudioByFile(context: Context, audioFile: Uri)
    fun stopAudioFile()
}