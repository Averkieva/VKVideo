package com.example.feature_video_list.data.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern
import kotlin.math.abs

private const val DEFAULT_DURATION = "0:00"
private const val UNKNOWN_DATE = "Неизвестная дата"
private const val JUST_NOW = "Только что"

private const val DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val TIME_ZONE_UTC = "UTC"

private val DURATION_PATTERN: Pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")

private const val TIME_FORMAT_HMS = "%d:%02d:%02d"
private const val TIME_FORMAT_MS = "%02d:%02d"

private const val YEARS_AGO = "назад"
private const val MONTHS_AGO = "назад"
private const val WEEKS_AGO = "назад"
private const val DAYS_AGO = "назад"
private const val HOURS_AGO = "назад"
private const val MINUTES_AGO = "назад"
private const val SECONDS_AGO = "назад"

private const val SECONDS_IN_MINUTE = 60L
private const val MINUTES_IN_HOUR = 60L
private const val HOURS_IN_DAY = 24L
private const val DAYS_IN_WEEK = 7L
private const val DAYS_IN_MONTH = 30L
private const val DAYS_IN_YEAR = 365L

private const val VIEW_SUFFIX = " просмотров"
private const val BILLION_SUFFIX = " млрд."
private const val MILLION_SUFFIX = " млн."
private const val THOUSAND_SUFFIX = " тыс."

private const val BILLION = 1_000_000_000L
private const val MILLION = 1_000_000L
private const val THOUSAND = 1_000L

fun formatDuration(isoDuration: String): String {
    val matcher = DURATION_PATTERN.matcher(isoDuration)
    return if (matcher.matches()) {
        val hours = matcher.group(1)?.toLongOrNull() ?: 0L
        val minutes = matcher.group(2)?.toLongOrNull() ?: 0L
        val seconds = matcher.group(3)?.toLongOrNull() ?: 0L
        formatTime(hours, minutes, seconds)
    } else {
        DEFAULT_DURATION
    }
}

@SuppressLint("DefaultLocale")
private fun formatTime(hours: Long, minutes: Long, seconds: Long): String {
    return if (hours > 0) {
        String.format(TIME_FORMAT_HMS, hours, minutes, seconds)
    } else {
        String.format(TIME_FORMAT_MS, minutes, seconds)
    }
}

fun formatRelativeDate(publishedAt: String): String {
    return try {
        val sdf = SimpleDateFormat(DATE_FORMAT_ISO, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(TIME_ZONE_UTC)
        val date = sdf.parse(publishedAt) ?: return UNKNOWN_DATE

        val now = Date()
        val diffInMillis = abs(now.time - date.time)

        val seconds = diffInMillis / 1000
        val minutes = seconds / SECONDS_IN_MINUTE
        val hours = minutes / MINUTES_IN_HOUR
        val days = hours / HOURS_IN_DAY
        val weeks = days / DAYS_IN_WEEK
        val months = days / DAYS_IN_MONTH
        val years = days / DAYS_IN_YEAR

        return when {
            years > 0 -> "$years ${pluralize(years, "год", "года", "лет")} $YEARS_AGO"
            months > 0 -> "$months ${pluralize(months, "месяц", "месяца", "месяцев")} $MONTHS_AGO"
            weeks > 0 -> "$weeks ${pluralize(weeks, "неделю", "недели", "недель")} $WEEKS_AGO"
            days > 0 -> "$days ${pluralize(days, "день", "дня", "дней")} $DAYS_AGO"
            hours > 0 -> "$hours ${pluralize(hours, "час", "часа", "часов")} $HOURS_AGO"
            minutes > 0 -> "$minutes ${pluralize(minutes, "минуту", "минуты", "минут")} $MINUTES_AGO"
            seconds > 0 -> "$seconds ${pluralize(seconds, "секунду", "секунды", "секунд")} $SECONDS_AGO"
            else -> JUST_NOW
        }
    } catch (e: Exception) {
        UNKNOWN_DATE
    }
}

fun pluralize(count: Long, one: String, few: String, many: String): String {
    val mod100 = count % 100
    val mod10 = count % 10
    return when {
        mod100 in 11..19 -> many
        mod10 == 1L -> one
        mod10 in 2..4 -> few
        else -> many
    }
}

@SuppressLint("DefaultLocale")
fun formatViewCount(viewCount: String): String {
    return try {
        val count = viewCount.toLongOrNull() ?: return viewCount
        when {
            count >= BILLION -> "${count / BILLION}$BILLION_SUFFIX$VIEW_SUFFIX"
            count >= MILLION -> "${count / MILLION}$MILLION_SUFFIX$VIEW_SUFFIX"
            count >= THOUSAND -> "${count / THOUSAND}$THOUSAND_SUFFIX$VIEW_SUFFIX"
            else -> "$count$VIEW_SUFFIX"
        }
    } catch (e: Exception) {
        viewCount
    }
}