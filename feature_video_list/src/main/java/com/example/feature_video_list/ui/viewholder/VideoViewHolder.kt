package com.example.feature_video_list.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feature_video_list.R
import com.example.feature_video_list.databinding.ItemVideoBinding
import com.example.feature_video_list.domain.model.VideoItem

class VideoViewHolder(private val binding: ItemVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(video: VideoItem) {
        binding.videoTitleTextView.text = video.title
        binding.videoInfoTextView.text = "${video.viewCount} просмотров • ${video.publishedDate}"
        binding.videoDurationTextView.text = video.duration

        Glide.with(binding.root.context)
            .load(video.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.cover_placeholder)
            .error(R.drawable.cover_placeholder)
            .into(binding.videoThumbnailImageView)
    }
}

