/*******************************************************************************
 *
 *  * Weather
 *  * Copyright (C) 2025 IconsNotFound
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Affero General Public License as
 *  * published by the Free Software Foundation, either version 3 of the
 *  * License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Affero General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Affero General Public License
 *  * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.iconsnotfound.weather.lib

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object TimeUtils {
    fun getCurrentTime(): Instant = Clock.System.now()

    fun getCurrentTimeInMillis(): Long = Clock.System.now().toEpochMilliseconds()

    fun hasOneHourPassed(lastUpdateStr: String): Boolean {
        return try {
            val tz = TimeZone.currentSystemDefault()
            val lastInstant = LocalDateTime.parse(lastUpdateStr).toInstant(tz)
            val now = Clock.System.now()
            val diff = now.toEpochMilliseconds() - lastInstant.toEpochMilliseconds()
            diff > 3_600_000
        } catch (_: Exception) {
            true
        }
    }

    fun hasTwentyMinutesPassed(lastUpdateStr: String): Boolean {
        return try {
            val tz = TimeZone.currentSystemDefault()
            val lastInstant = LocalDateTime.parse(lastUpdateStr).toInstant(tz)
            val now = Clock.System.now()
            val diff = now.toEpochMilliseconds() - lastInstant.toEpochMilliseconds()
            diff > 1_200_000
        } catch (_: Exception) {
            true
        }
    }

    fun isNightNow(
        sunriseIso: String? = null,
        sunsetIso: String? = null
    ): Boolean {
        val zone = TimeZone.currentSystemDefault()
        val currentTimeInstant = Clock.System.now()
        val currentTime = currentTimeInstant.toLocalDateTime(zone)
        if (sunriseIso.isNullOrBlank() || sunsetIso.isNullOrBlank()) {
            val hour = currentTime.hour
            return hour < 6 || hour >= 18
        }

        val sunriseTime = LocalDateTime.parse(sunriseIso).toInstant(zone).toEpochMilliseconds()
        val sunsetTime = LocalDateTime.parse(sunsetIso).toInstant(zone).toEpochMilliseconds()
        val nowTime = currentTimeInstant.toEpochMilliseconds()
        return nowTime < sunriseTime || nowTime >= sunsetTime
    }

    fun sunsetSunriseTime(
        sunrise: List<String>,
        sunset: List<String>,
        _06_00: String = "06:00",
        _0: String = "0",
        am: String = "AM",
        pm: String = "PM",
    ): Triple<String, String, Pair<Float, Boolean>> {
        val currentTimeInstant = Clock.System.now()
        val currentTime = currentTimeInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        if (sunrise.isEmpty() || sunset.isEmpty()) {
            val hour = currentTime.hour
            val isNight = hour < 6 || hour >= 18
            val a = if (isNight) "$_06_00 $pm" else "$_06_00 $am"
            val b = if (isNight) "$_06_00 $am" else "$_06_00 $pm"
            return Triple(a, b, Pair(0f, false))
        }
        val isNightNow = isNightNow(sunrise.first(), sunset.first())
        if(isNightNow && sunrise.size<2) {
            return Triple(sunset.first(), "$_06_00 $am", Pair(0f, false))
        }
        val zone = TimeZone.currentSystemDefault()
        val startTime = LocalDateTime.parse(sunrise.first()).toInstant(zone)
        val endTime = LocalDateTime.parse(sunset.first()).toInstant(zone)
        val duration = (endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds())
        val elapsed = currentTimeInstant.toEpochMilliseconds() - startTime.toEpochMilliseconds()
        val ratio = elapsed.toFloat() / duration

        return Triple(
            formatTo12Hour(
                instant = startTime,
                _0 = _0,
                am = am,
                pm = pm
            ),
            formatTo12Hour(
                instant = endTime,
                _0 = _0,
                am = am,
                pm = pm
            ),
            Pair(ratio.coerceIn(0f, 1f), isNightNow)
        )
    }

    fun formatTo12Hour(
        instant: Instant,
        _0: String = "0",
        am: String = "AM",
        pm: String = "PM",
    ): String {
        val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        var hour = dt.hour
        val minute = dt.minute
        val amPm = if (hour < 12) am else pm

        if (hour == 0) hour = 12
        else if (hour > 12) hour -= 12

        val minuteStr = minute.toString().padWithLeading(_0)
        val hourStr = hour.toString().padWithLeading(_0)

        return "$hourStr:$minuteStr $amPm"
    }


    fun formatSmartTime(
        input: String,
        _0: String = "0",
        am: String = "AM",
        pm: String = "PM",
        yesterday: String = "Yesterday"
    ): String {
        val inputTime = LocalDateTime.parse(input)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val inputDate = inputTime.date
        val currentDate = now.date

        return when {
            inputDate == currentDate -> {
                val hour24 = inputTime.hour
                val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12
                val minute = inputTime.minute.toString().padWithLeading(_0)
                val ampm = if (hour24 < 12) am else pm
                "$hour12:$minute $ampm"
            }

            inputDate == currentDate.minus(DatePeriod(days = 1)) -> {
                yesterday
            }

            inputDate.year == currentDate.year -> {
                "${inputDate.dayOfMonth} ${inputDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)}"
            }

            else -> {
                "${inputDate.dayOfMonth} ${inputDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)} ${inputDate.year}"
            }
        }
    }
}