package com.example.feature_video_list.data.model

data class PexelsVideo(
    val id: Int,
    val width: Int,
    val height: Int,
    val duration: Int,
    val url: String,
    val image: String,
    val video_files: List<PexelsVideoFile>,
    val video_pictures: List<PexelsVideoPicture>,
    val user: PexelsUser
)