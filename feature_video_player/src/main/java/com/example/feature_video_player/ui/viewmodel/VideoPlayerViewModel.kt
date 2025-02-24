package com.example.feature_video_player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feature_video_player.domain.VideoPlayerRepository
import com.google.android.exoplayer2.MediaItem

class VideoPlayerViewModel(private val repository: VideoPlayerRepository) : ViewModel() {

    private val _videoUrls = MutableLiveData<List<String>>()

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    fun setVideoUrls(urls: List<String>, startIndex: Int) {
        _videoUrls.value = urls
        _currentIndex.value = startIndex
    }

    fun getMediaItems(): List<MediaItem> {
        return repository.getMediaItems(_videoUrls.value.orEmpty())
    }
}
