package com.example.feature_video_list.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.domain.repository.VideoRepository
import kotlinx.coroutines.launch
import java.io.IOException

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {

    private val _videos = MutableLiveData<List<VideoItem>>()
    val videos: LiveData<List<VideoItem>> get() = _videos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentQuery: String? = null
    private var isSearchMode = false

    private var currentPage = 1
    var isLastPage = false
    val pageSize = DEFAULT_PAGE_SIZE

    init {
        loadPopularVideos()
    }

    fun loadPopularVideos() {
        if (!isSearchMode) {
            _isLoading.value = true
            currentPage = 1
            viewModelScope.launch {
                try {
                    val videos = repository.getPopularVideos(page = currentPage)
                    if (videos.isEmpty()) {
                        _error.value = ERROR_NO_VIDEOS_FOUND
                    } else {
                        _videos.value = videos
                        _error.value = null
                    }
                    isSearchMode = false
                    currentQuery = null
                    isLastPage = videos.size < pageSize
                } catch (e: IOException) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun loadMoreVideos() {
        if (_isLoading.value == true || isLastPage) return

        _isLoading.value = true
        currentPage++
        viewModelScope.launch {
            try {
                val newVideos = if (isSearchMode && !currentQuery.isNullOrEmpty()) {
                    repository.searchVideos(currentQuery!!, page = currentPage)
                } else {
                    repository.getPopularVideos(page = currentPage)
                }

                val updatedVideos = _videos.value.orEmpty() + newVideos
                _videos.value = updatedVideos
                isLastPage = newVideos.size < pageSize
            } catch (e: Exception) {
                _error.value = e.message
                currentPage--
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchVideos(query: String) {
        if (query != currentQuery) {
            _isLoading.value = true
            currentPage = 1
            viewModelScope.launch {
                try {
                    val videos = repository.searchVideos(query, page = currentPage)
                    if (videos.isEmpty()) {
                        _error.value = ERROR_NO_VIDEOS_FOUND
                    } else {
                        _videos.value = videos
                        _error.value = null
                    }
                    currentQuery = query
                    isSearchMode = true
                    isLastPage = videos.size < pageSize
                } catch (e: Exception) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun restoreLastState() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                if (isSearchMode && !currentQuery.isNullOrEmpty()) {
                    val videos = repository.searchVideos(currentQuery!!, page = currentPage)
                    _videos.value = videos
                } else {
                    val videos = repository.getPopularVideos(page = currentPage)
                    _videos.value = videos
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        isSearchMode = false
        currentQuery = null
        currentPage = 1
        isLastPage = false
        loadPopularVideos()
    }

    companion object {
        const val ERROR_NO_VIDEOS_FOUND = "Нет видео по вашему запросу"
        const val DEFAULT_PAGE_SIZE = 10
    }
}


