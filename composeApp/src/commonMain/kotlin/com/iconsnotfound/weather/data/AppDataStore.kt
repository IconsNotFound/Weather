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

package com.iconsnotfound.weather.data

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.network.weather.DailyWeather
import com.iconsnotfound.weather.lib.TimeUtils
import com.iconsnotfound.weather.showToast

object AppDataStore {
    private const val KEY = "saved_places"

    private const val MAX_SAVED_PLACES = 10
    private const val BASE_LIMIT = 30
    private const val BASE_ATTEMPT_LIMIT = 50
    private const val DELAY_STEP_SECONDS = 2
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val SEARCH_ATTEMPT_KEY = "search_attempts"
    private const val THEME_KEY = "theme"
    private const val DEFAULT_PLACE_KEY = "default_place_id"

    object THEME {
        const val SYSTEM = 0
        const val LIGHT = 1
        const val DARK = 2
    }

    fun saveAll(places: List<Places>, settings: Settings) {
        val json = Json.encodeToString(places)
        settings.putString(KEY, json)
    }

    fun loadAll(settings: Settings): List<Places> {
        val json = settings.getStringOrNull(KEY) ?: return emptyList()
        return runCatching {
            Json.decodeFromString<List<Places>>(json)
        }.getOrElse { emptyList() }
    }

    fun getTotalSavedPlaces(settings: Settings): Int {
        val json = settings.getStringOrNull(KEY) ?: return 0
        return runCatching {
            Json.decodeFromString<List<Places>>(json).size
        }.getOrElse { 0 }
    }

    fun hasSavedPlacesLimitReached(settings: Settings): Pair<Boolean, Int> {
        return Pair(getTotalSavedPlaces(settings) >= MAX_SAVED_PLACES, MAX_SAVED_PLACES)
    }

    fun add(place: Places, settings: Settings, msgSuccess: String = "Success") {
        val updated = loadAll(settings).toMutableList().apply { add(place) }
        saveAll(updated, settings)
        showToast(msgSuccess)
    }

    fun delete(id: String, settings: Settings, msgSuccess: String = "Deleted") {
        val updated = loadAll(settings).filterNot { it.id == id }
        saveAll(updated, settings)
        showToast(msgSuccess)
    }

    fun updateWeather(id: String, currentWeather: CurrentWeather, dailyWeather: DailyWeather, settings: Settings) {
        val updated = loadAll(settings).map {
            if (it.id == id) it.copy(currentWeather = currentWeather, dailyWeather = dailyWeather) else it
        }
        saveAll(updated, settings)
    }

    fun exists(id: String, settings: Settings): Boolean {
        return loadAll(settings).any { it.id == id }
    }

    fun setDefaultPlace(id: String, settings: Settings, msgSuccess: String = "Place updated") {
        settings.putString(DEFAULT_PLACE_KEY, id)
        showToast(msgSuccess)
    }

    fun getDefaultPlace(settings: Settings): Places? {
        val id = settings.getStringOrNull(DEFAULT_PLACE_KEY) ?: return null
        return loadAll(settings).find { it.id == id }
    }

    fun isDefaultPlace(id: String, settings: Settings): Boolean {
        return settings.getStringOrNull(DEFAULT_PLACE_KEY) == id
    }

    fun clearDefaultPlace(settings: Settings) {
        settings.putString(DEFAULT_PLACE_KEY, "")
    }

    fun recordSearch(settings: Settings) {
        val currentTime = TimeUtils.getCurrentTimeInMillis()

        val oldList = settings.getStringOrNull(SEARCH_HISTORY_KEY)
            ?.split(",")
            ?.mapNotNull { it.toLongOrNull() }
            ?: emptyList()

        val updatedList = (oldList + currentTime)
            .filter { it > currentTime - 3600_000 }

        settings.putString(SEARCH_HISTORY_KEY, updatedList.joinToString(","))
    }

    fun recordSearchAttempt(settings: Settings) {
        val currentTime = TimeUtils.getCurrentTimeInMillis()

        val oldList = settings.getStringOrNull(SEARCH_ATTEMPT_KEY)
            ?.split(",")
            ?.mapNotNull { it.toLongOrNull() }
            ?: emptyList()

        val updatedList = (oldList + currentTime)
            .filter { it > currentTime - 3600_000 }

        settings.putString(SEARCH_ATTEMPT_KEY, updatedList.joinToString(","))
    }


    fun getSearchCountLastHour(settings: Settings): Int {
        val currentTime = TimeUtils.getCurrentTimeInMillis()

        return settings.getStringOrNull(SEARCH_HISTORY_KEY)
            ?.split(",")
            ?.mapNotNull { it.toLongOrNull() }
            ?.count { it > currentTime - 3600_000 }
            ?: 0
    }

    fun getSearchAttemptCountLastHour(settings: Settings): Int {
        val currentTime = TimeUtils.getCurrentTimeInMillis()

        return settings.getStringOrNull(SEARCH_ATTEMPT_KEY)
            ?.split(",")
            ?.mapNotNull { it.toLongOrNull() }
            ?.count { it > currentTime - 3600_000 }
            ?: 0
    }

    fun hasSearchCrossedRateLimit(settings: Settings): Boolean = getSearchCountLastHour(settings) > BASE_LIMIT

    fun hasSearchAttemptCrossedRateLimit(settings: Settings): Boolean = getSearchAttemptCountLastHour(settings) > BASE_ATTEMPT_LIMIT

    fun getSearchRateLimit(settings: Settings): Long {
        val count = getSearchCountLastHour(settings)
        val countAttempt = getSearchAttemptCountLastHour(settings)

        if (count > BASE_LIMIT) {
            val extraSearches = count - BASE_LIMIT
            val delayMillis = extraSearches * DELAY_STEP_SECONDS * 1000L
            return delayMillis
        }
        else if(countAttempt > BASE_ATTEMPT_LIMIT) {
            val extraSearches = countAttempt - BASE_ATTEMPT_LIMIT
            val delayMillis = extraSearches * DELAY_STEP_SECONDS * 1000L
            return delayMillis
        }
        return 0L
    }

    fun getTheme(settings: Settings): Int {
        var theme = settings.getIntOrNull(THEME_KEY)
        if(theme == null) {
            setTheme(settings)
            theme = settings.getIntOrNull(THEME_KEY)
        }
        return theme?: THEME.SYSTEM
    }

    fun setTheme(settings: Settings, code: Int = THEME.SYSTEM) {
        settings.putInt(THEME_KEY, code)
    }

}