package com.example.feature_video_list.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.feature_video_list.databinding.ItemVideoBinding
import com.example.feature_video_list.domain.model.VideoItem

class VideoViewHolder(private val binding: ItemVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(video: VideoItem) {
        binding.videoTitleTextView.text = video.title
        binding.videoDurationTextView.text = video.duration
        binding.trackCoverImageView.setImageResource(video.imageResId)
    }
}

