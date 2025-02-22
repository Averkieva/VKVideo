package com.example.feature_video_list.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_video_list.databinding.FragmentVideoListBinding
import com.example.feature_video_list.ui.adapter.VideoAdapter
import com.example.feature_video_list.ui.viewmodel.VideoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoListFragment : Fragment() {

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    private val viewModel: VideoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.loadPopularVideos()

        binding.root.setOnTouchListener { v, _ ->
            hideKeyboard(v)
            false
        }

        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = VideoAdapter(emptyList())
        binding.videoRecyclerView.adapter = adapter
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard(recyclerView)
                }
            }
        })
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isEmpty()) {
                viewModel.loadPopularVideos()
            } else {
                viewModel.searchVideos(query)
            }
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            viewModel.searchVideos(query)
        }
    }

    private fun setupObservers() {
        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.updateData(videos)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
