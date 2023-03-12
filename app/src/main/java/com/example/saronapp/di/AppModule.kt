package com.example.saronapp.di

import com.example.saronapp.data.api.RetrofitService
import com.example.saronapp.data.repository.MainRepository
import com.example.saronapp.data.repository.MainRepositoryImpl
import com.example.saronapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): RetrofitService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRepositoryImpl(
        retrofitService: RetrofitService
    ): MainRepository =  MainRepositoryImpl(retrofitService)
}