package com.example.saronapp.data.repository

import com.example.saronapp.data.api.RetrofitService
import com.example.saronapp.data.model.Prediction
import com.example.saronapp.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepositoryImpl constructor(private val retrofitService: RetrofitService) : MainRepository {
    override suspend fun predictBilah(
        expectedBilah: RequestBody,
        audio: MultipartBody.Part
    ): Resource<Prediction> {
        return try {
            val response = retrofitService.predictBilah(expectedBilah, audio)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody?.status == "success") {
                Resource.success(responseBody.data)
            } else {
                Resource.error(response.message())
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString())
        }

    }
}