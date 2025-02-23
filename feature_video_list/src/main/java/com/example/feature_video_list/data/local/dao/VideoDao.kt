package com.example.feature_video_list.data.local.dao

import androidx.room.*
import com.example.feature_video_list.data.local.entity.VideoItemEntity

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): List<VideoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoItemEntity>)

    @Query("DELETE FROM videos")
    suspend fun clearVideos()
}
