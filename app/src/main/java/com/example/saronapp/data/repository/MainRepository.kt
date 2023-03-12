package com.example.saronapp.data.repository

import com.example.saronapp.data.model.Prediction
import com.example.saronapp.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MainRepository {
    suspend fun predictBilah(
        expectedBilah: RequestBody,
        audio: MultipartBody.Part
    ): Resource<Prediction>
}