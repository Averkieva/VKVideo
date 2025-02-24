package com.example.feature_video_player.di

import com.example.feature_video_player.data.repository.VideoPlayerRepositoryImpl
import com.example.feature_video_player.domain.VideoPlayerRepository
import com.example.feature_video_player.ui.viewmodel.VideoPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val videoPlayerModule = module {

    single<VideoPlayerRepository> { VideoPlayerRepositoryImpl() }

    viewModel { VideoPlayerViewModel(repository = get()) }
}