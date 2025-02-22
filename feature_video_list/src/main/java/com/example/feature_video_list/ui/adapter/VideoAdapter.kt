package com.example.feature_video_list.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_video_list.databinding.ItemVideoBinding
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.ui.viewholder.VideoViewHolder

class VideoAdapter(private var videos: List<VideoItem>) :
    RecyclerView.Adapter<VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newVideos: List<VideoItem>) {
        videos = newVideos
        notifyDataSetChanged()
    }
}