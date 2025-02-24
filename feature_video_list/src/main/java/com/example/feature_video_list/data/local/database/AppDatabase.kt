package com.example.feature_video_list.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.feature_video_list.data.local.dao.VideoDao
import com.example.feature_video_list.data.local.entity.VideoItemEntity

@Database(entities = [VideoItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}