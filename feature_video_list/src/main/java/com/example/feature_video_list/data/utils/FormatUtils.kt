package com.example.feature_video_list.data.utils

import android.annotation.SuppressLint

class FormatUtils {

    companion object {
        private const val SECONDS_IN_MINUTE = 60
        private const val SECONDS_IN_HOUR = 3600
        private const val TIME_FORMAT_HMS = "%02d:%02d:%02d"
        private const val TIME_FORMAT_MS = "%02d:%02d"

        @SuppressLint("DefaultLocale")
        fun formatDuration(durationSeconds: Int): String {
            val hours = durationSeconds / SECONDS_IN_HOUR
            val minutes = (durationSeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
            val seconds = durationSeconds % SECONDS_IN_MINUTE

            return if (hours > 0) {
                String.format(TIME_FORMAT_HMS, hours, minutes, seconds)
            } else {
                String.format(TIME_FORMAT_MS, minutes, seconds)
            }
        }
    }
}
