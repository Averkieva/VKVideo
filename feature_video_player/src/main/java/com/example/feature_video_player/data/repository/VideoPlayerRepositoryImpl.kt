package com.example.feature_video_player.data.repository

import com.example.feature_video_player.domain.VideoPlayerRepository
import com.google.android.exoplayer2.MediaItem

class VideoPlayerRepositoryImpl: VideoPlayerRepository {

    override fun getMediaItems(videoUrls: List<String>): List<MediaItem> {
        return videoUrls.map { url ->
            MediaItem.fromUri(url)
        }
    }
}
