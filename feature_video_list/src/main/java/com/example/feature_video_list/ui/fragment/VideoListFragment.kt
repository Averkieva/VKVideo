package com.example.feature_video_list.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.feature_video_list.R
import com.example.feature_video_list.databinding.FragmentVideoListBinding
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.ui.adapter.VideoAdapter

class VideoListFragment : Fragment() {
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VideoAdapter(getMockVideos())
        binding.videoRecyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshVideos()
        }
    }

    private fun refreshVideos() {
        binding.swipeRefreshLayout.isRefreshing = true
        binding.videoRecyclerView.postDelayed({
            adapter.updateData(getMockVideos())
            binding.swipeRefreshLayout.isRefreshing = false
        }, 1500)
    }

    private fun getMockVideos(): List<VideoItem> {
        return listOf(
            VideoItem("Видео 1", "3:45", R.drawable.cover_placeholder),
            VideoItem("Видео 2", "7:20", R.drawable.cover_placeholder),
            VideoItem("Видео 3", "5:10", R.drawable.cover_placeholder)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
