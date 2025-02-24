package com.example.feature_video_list.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_video_list.R
import com.example.feature_video_list.databinding.FragmentVideoListBinding
import com.example.feature_video_list.ui.adapter.VideoAdapter
import com.example.feature_video_list.ui.viewmodel.VideoViewModel
import com.example.feature_video_player.ui.fragment.VideoPlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoListFragment : Fragment() {

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    private val viewModel: VideoViewModel by viewModel()

    private var isReturningFromPlayer = false

    private var isLoadingMore = false

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
        adapter = VideoAdapter(emptyList()) { videoItem ->
            isReturningFromPlayer = true

            val videoUrls = viewModel.videos.value?.map { it.videoUrl } ?: emptyList()
            val currentIndex = viewModel.videos.value?.indexOf(videoItem) ?: 0

            val fragment = VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_VIDEO_URLS, ArrayList(videoUrls))
                    putInt(ARG_VIDEO_INDEX, currentIndex)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.videoRecyclerView.adapter = adapter
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && !viewModel.isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= viewModel.pageSize
                    ) {
                        viewModel.loadMoreVideos()
                    }
                }
            }
        })
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.restoreLastState()
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchVideos(query)
            } else {
                viewModel.clearSearch()
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.clearSearch()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupObservers() {
        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.updateData(videos)
            isLoadingMore = false
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isReturningFromPlayer) {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isEmpty()) {
                viewModel.clearSearch()
            } else {
                viewModel.restoreLastState()
            }
        }
        isReturningFromPlayer = false
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_VIDEO_URLS = "video_urls"
        const val ARG_VIDEO_INDEX = "video_index"
    }
}

