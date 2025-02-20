package com.example.feature_video_list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_video_list.domain.repository.VideoRepository

class VideoViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}