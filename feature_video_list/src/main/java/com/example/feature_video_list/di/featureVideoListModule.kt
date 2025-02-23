package com.example.feature_video_list.di

import com.example.feature_video_list.R
import com.example.feature_video_list.data.api.PexelsApiService
import com.example.feature_video_list.data.repository.VideoRepositoryImpl
import com.example.feature_video_list.domain.repository.VideoRepository
import com.example.feature_video_list.ui.viewmodel.VideoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val featureVideoListModule = module {

    single<String> {
        androidContext().getString(R.string.youtube_api_key)
    }

    single<PexelsApiService> {
        Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }

    single<VideoRepository> {
        VideoRepositoryImpl(apiService = get(), apiKey = get())
    }

    viewModel { VideoViewModel(repository = get()) }
}
