package com.example.saronapp.data.media

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import javax.inject.Inject

@Suppress("DEPRECATION")
class AudioPlayerImpl @Inject constructor() : AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun playAudioFile(context: Context, audioResId: Int) {
        mediaPlayer = MediaPlayer.create(context, audioResId)
        mediaPlayer?.start()
    }

    override fun playAudioByFile(context: Context, audioFile: Uri) {
        Log.d("audioPlayer", "$audioFile")
        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(context, audioFile)
            prepare()
            start()
        }
    }

    override fun stopAudioFile() {
        mediaPlayer?.stop()
    }
}