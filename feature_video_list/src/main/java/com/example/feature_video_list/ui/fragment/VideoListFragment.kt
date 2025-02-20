package com.example.feature_video_list.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feature_video_list.data.RetrofitClient
import com.example.feature_video_list.data.YouTubePopularVideo
import com.example.feature_video_list.data.YouTubeResponse
import com.example.feature_video_list.data.YouTubeSearchVideo
import com.example.feature_video_list.databinding.FragmentVideoListBinding
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.ui.adapter.VideoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

class VideoListFragment : Fragment() {

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter
    private val apiService = RetrofitClient.instance
    private val apiKey = "AIzaSyChvnJudDSvNBkOM3-a3FVfxgpJS41a_XY"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()

        refreshVideos("")
    }

    private fun setupRecyclerView() {
        adapter = VideoAdapter(emptyList())
        binding.videoRecyclerView.adapter = adapter
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val query = binding.searchEditText.text.toString().trim()
            refreshVideos(query)
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            Log.d("SEARCH_DEBUG", "Поиск запущен с запросом: '$query'")
            refreshVideos(query)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    refreshVideos("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun refreshVideos(query: String) {
        binding.swipeRefreshLayout.isRefreshing = true

        if (query.isEmpty()) {
            Log.d("API_DEBUG", "Запрашиваем популярные видео")

            apiService.getPopularVideos(apiKey = apiKey)
                .enqueue(object : Callback<YouTubeResponse<YouTubePopularVideo>> {
                    override fun onResponse(
                        call: Call<YouTubeResponse<YouTubePopularVideo>>,
                        response: Response<YouTubeResponse<YouTubePopularVideo>>
                    ) {
                        if (response.isSuccessful) {
                            val items = response.body()?.items?.map { video ->
                                val thumbnailUrl = video.snippet.thumbnails?.maxres?.url
                                    ?: video.snippet.thumbnails?.standard?.url
                                    ?: video.snippet.thumbnails?.high?.url
                                    ?: video.snippet.thumbnails?.medium?.url ?: ""

                                VideoItem(
                                    title = video.snippet.title,
                                    imageUrl = thumbnailUrl,
                                    duration = formatDuration(video.contentDetails.duration),
                                    viewCount = "${video.statistics.viewCount}",
                                    publishedDate = formatPublishedDate(video.snippet.publishedAt)
                                )
                            } ?: emptyList()

                            adapter.updateData(items)
                            Log.d("API_SUCCESS", "Топовые видео загружены: ${items.size}")
                        } else {
                            Log.e("API_ERROR", "Ошибка API: ${response.errorBody()?.string()}")
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(
                        call: Call<YouTubeResponse<YouTubePopularVideo>>,
                        t: Throwable
                    ) {
                        Log.e("API_ERROR", "Ошибка загрузки: ${t.localizedMessage}")
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                })

        } else {
            Log.d("API_DEBUG", "Запрашиваем поиск с запросом: '$query'")

            apiService.getVideos(query = query, apiKey = apiKey)
                .enqueue(object : Callback<YouTubeResponse<YouTubeSearchVideo>> {
                    override fun onResponse(
                        call: Call<YouTubeResponse<YouTubeSearchVideo>>,
                        response: Response<YouTubeResponse<YouTubeSearchVideo>>
                    ) {
                        if (response.isSuccessful) {
                            val videoIds =
                                response.body()?.items?.map { it.id.videoId }?.joinToString(",")
                                    ?: ""

                            if (videoIds.isNotEmpty()) {
                                apiService.getVideoDetails(videoIds = videoIds, apiKey = apiKey)
                                    .enqueue(object :
                                        Callback<YouTubeResponse<YouTubePopularVideo>> {
                                        override fun onResponse(
                                            call: Call<YouTubeResponse<YouTubePopularVideo>>,
                                            response: Response<YouTubeResponse<YouTubePopularVideo>>
                                        ) {
                                            if (response.isSuccessful) {
                                                val items = response.body()?.items?.map { video ->
                                                    val thumbnailUrl =
                                                        video.snippet.thumbnails?.maxres?.url
                                                            ?: video.snippet.thumbnails?.standard?.url
                                                            ?: video.snippet.thumbnails?.high?.url
                                                            ?: video.snippet.thumbnails?.medium?.url
                                                            ?: ""

                                                    VideoItem(
                                                        title = video.snippet.title,
                                                        imageUrl = thumbnailUrl,
                                                        duration = formatDuration(video.contentDetails.duration),
                                                        viewCount = "${video.statistics.viewCount}",
                                                        publishedDate = formatPublishedDate(video.snippet.publishedAt)
                                                    )
                                                } ?: emptyList()

                                                adapter.updateData(items)
                                                Log.d(
                                                    "API_SUCCESS",
                                                    "Видео по запросу загружены: ${items.size}"
                                                )
                                            } else {
                                                Log.e(
                                                    "API_ERROR",
                                                    "Ошибка API: ${response.errorBody()?.string()}"
                                                )
                                            }
                                            binding.swipeRefreshLayout.isRefreshing = false
                                        }

                                        override fun onFailure(
                                            call: Call<YouTubeResponse<YouTubePopularVideo>>,
                                            t: Throwable
                                        ) {
                                            Log.e(
                                                "API_ERROR",
                                                "Ошибка загрузки: ${t.localizedMessage}"
                                            )
                                            binding.swipeRefreshLayout.isRefreshing = false
                                        }
                                    })
                            }
                        } else {
                            Log.e("API_ERROR", "Ошибка API: ${response.errorBody()?.string()}")
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                    }

                    override fun onFailure(
                        call: Call<YouTubeResponse<YouTubeSearchVideo>>,
                        t: Throwable
                    ) {
                        Log.e("API_ERROR", "Ошибка загрузки: ${t.localizedMessage}")
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                })
        }
    }

    fun formatPublishedDate(publishedAt: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(publishedAt)
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Неизвестная дата"
        }
    }

    fun formatDuration(isoDuration: String): String {
        val pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
        val matcher = pattern.matcher(isoDuration)
        return if (matcher.matches()) {
            val hours = matcher.group(1)?.toLongOrNull() ?: 0
            val minutes = matcher.group(2)?.toLongOrNull() ?: 0
            val seconds = matcher.group(3)?.toLongOrNull() ?: 0

            when {
                hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
                else -> String.format("%02d:%02d", minutes, seconds)
            }
        } else {
            "0:00"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
