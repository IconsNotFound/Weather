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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.iconsnotfound.weather.lib.TimeUtils
import com.iconsnotfound.weather.network.weather.WeatherApi
import com.iconsnotfound.weather.showToast
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout

class WeatherRepository(
    initialPlaces: List<Places>,
    private val settings: Settings,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    var dataUpdated by mutableStateOf(false)
        private set

    private val _places = mutableStateListOf<Places>().apply { addAll(initialPlaces) }
    val places: List<Places> get() = _places

    var isLoading by mutableStateOf(false)
        private set

    private var searchJob: Job? = null

    fun refreshWeather(manualRefresh: Boolean = false, msgSuccess: String, userAgentString: String) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            if(manualRefresh) delay(500)

            val allFresh = _places.all { loc ->
                val lastUpdatedStr = loc.currentWeather?.time ?: ""
                lastUpdatedStr.isNotEmpty() && !(if(!manualRefresh) TimeUtils.hasOneHourPassed(lastUpdatedStr) else TimeUtils.hasTwentyMinutesPassed(lastUpdatedStr))
            }
            if (allFresh) {
                dataUpdated = true
                if(manualRefresh) showToast(msgSuccess)
                return@launch
            }

            isLoading = true
            try {
                supervisorScope {
                    val results = try {
                        withTimeout(5000) {
                            WeatherApi.getWeatherForMultiplePlaces(_places, userAgentString)
                        }
                    } catch (_: Exception) {
                        _places.map { it to null }
                    }

                    results.forEach { (place, weather) ->
                        if (weather != null) {
                            AppDataStore.updateWeather(place.id, weather.current, weather.daily, settings)
                            val index = _places.indexOfFirst { it.id == place.id }
                            if (index >= 0) {
                                _places[index] = _places[index].copy(currentWeather = weather.current)
                            }
                        }
                    }
                    dataUpdated = true
                    if(manualRefresh) showToast(msgSuccess)
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun resetDataUpdatedFlag() {
        dataUpdated = false
    }
}