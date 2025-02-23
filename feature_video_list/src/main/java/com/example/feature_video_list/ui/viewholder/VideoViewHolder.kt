package com.example.feature_video_list.ui.viewholder

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feature_video_list.R
import com.example.feature_video_list.data.utils.FormatUtils
import com.example.feature_video_list.databinding.ItemVideoBinding
import com.example.feature_video_list.domain.model.VideoItem

class VideoViewHolder(
    private val binding: ItemVideoBinding,
    private val onItemClick: (VideoItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("StringFormatMatches")
    fun bind(video: VideoItem) {
        binding.videoTitleTextView.text = video.title

        binding.videoInfoTextView.text = binding.root.context.getString(
            R.string.video_info_format,
            video.authorName,
            video.duration
        )

        binding.videoDurationTextView.text = FormatUtils.formatDuration(video.duration)

        Glide.with(binding.root.context)
            .load(video.thumbnailUrl)
            .centerCrop()
            .placeholder(R.drawable.cover_placeholder)
            .error(R.drawable.cover_placeholder)
            .into(binding.videoThumbnailImageView)

        binding.root.setOnClickListener {
            if (video.videoUrl.isNotEmpty()) {
                onItemClick(video)
            } else {
                Toast.makeText(
                    binding.root.context,
                    ERROR_URL_NOT_FOUND,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val ERROR_URL_NOT_FOUND = "URL видео отсутствует"
    }
}

