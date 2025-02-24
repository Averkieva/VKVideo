package com.example.feature_video_player.domain

import com.google.android.exoplayer2.MediaItem

interface VideoPlayerRepository {
    fun getMediaItems(videoUrls: List<String>): List<MediaItem>
}