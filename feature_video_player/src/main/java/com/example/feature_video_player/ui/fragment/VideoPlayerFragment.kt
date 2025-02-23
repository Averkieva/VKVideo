package com.example.feature_video_player.ui.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.feature_video_player.databinding.FragmentVideoPlayerBinding
import com.example.feature_video_player.ui.viewmodel.VideoPlayerViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoPlayerFragment : Fragment() {

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private val viewModel: VideoPlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrls = arguments?.getStringArrayList(ARG_VIDEO_URLS) ?: emptyList()
        val startIndex = arguments?.getInt(ARG_VIDEO_INDEX) ?: 0

        if (videoUrls.isEmpty()) {
            Toast.makeText(requireContext(), EMPTY_VIDEO_LIST_MESSAGE, Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.setVideoUrls(videoUrls, startIndex)
        setupObservers()

        setupBackButton()
    }

    private fun setupObservers() {
        viewModel.currentIndex.observe(viewLifecycleOwner, Observer { index ->
            initializePlayer(index)
        })
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initializePlayer(currentIndex: Int) {
        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

            val mediaItems = viewModel.getMediaItems()
            exoPlayer.setMediaItems(mediaItems, currentIndex, 0L)

            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        enterFullScreen()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        exitFullScreen()
    }

    private fun enterFullScreen() {
        val window = requireActivity().window
        ViewCompat.getWindowInsetsController(window.decorView)?.let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun exitFullScreen() {
        val window = requireActivity().window
        ViewCompat.getWindowInsetsController(window.decorView)?.let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_VIDEO_URLS = "video_urls"
        const val ARG_VIDEO_INDEX = "video_index"
        const val EMPTY_VIDEO_LIST_MESSAGE = "Список видео пуст"
    }
}
