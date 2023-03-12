package com.example.saronapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prediction (
    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("expected")
    val expected: String? = null,

    @field:SerializedName("audio_path")
    val audioPath: String? = null
) : Parcelable