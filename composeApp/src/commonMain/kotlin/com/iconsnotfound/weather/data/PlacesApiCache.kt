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

import com.iconsnotfound.weather.lib.TimeUtils
import com.iconsnotfound.weather.network.places.PlacesModels
import com.russhwolf.settings.Settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PlacesCache(
    val query: String,
    val results: List<PlacesModels>,
    val timestamp: Long = TimeUtils.getCurrentTimeInMillis()
)

private object CacheKeys {
    const val CACHE_PLACE = "cache_place_"
}

class ApiCache(private val settings: Settings) {
    private val json = Json { ignoreUnknownKeys = true }
    private val cacheExpiryMillis = 24 * 60 * 60 * 1000

    fun get(query: String): List<PlacesModels>? {
        val cachedString = settings.getStringOrNull("${CacheKeys.CACHE_PLACE}$query") ?: return null
        return try {
            val cache = json.decodeFromString<PlacesCache>(cachedString)

            if (TimeUtils.getCurrentTimeInMillis() - cache.timestamp > cacheExpiryMillis) {
                settings.remove("${CacheKeys.CACHE_PLACE}$query")
                null
            } else {
                cache.results
            }
        } catch (_: Exception) {
            settings.remove("${CacheKeys.CACHE_PLACE}$query")
            null
        }
    }

    fun put(query: String, results: List<PlacesModels>) {
        val cache = PlacesCache(query, results)
        settings.putString("${CacheKeys.CACHE_PLACE}$query", json.encodeToString(cache))
    }

    fun clear() {
        settings.keys
            .filter { it.startsWith(CacheKeys.CACHE_PLACE) }
            .forEach { settings.remove(it) }
    }
}