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

package com.iconsnotfound.weather.network.weather

import com.iconsnotfound.weather.config.ApiConfig
import com.iconsnotfound.weather.createHttpClient
import com.iconsnotfound.weather.data.Places
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json

object WeatherApi {
    private val client = createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val currentFields = listOf(
        "temperature_2m",
        "weather_code",
        "wind_speed_10m",
        "apparent_temperature",
        "relative_humidity_2m",
        "wind_direction_10m",
        "surface_pressure",
        "visibility",
        "uv_index",
        "precipitation",
        "dew_point_2m",
        "cloud_cover",
        "wind_gusts_10m",
        "shortwave_radiation"
    ).joinToString(",")
    private val dailyFields = listOf(
        "sunrise",
        "sunset"
    ).joinToString(",")

    suspend fun getWeatherForMultiplePlaces(
        places: List<Places>,
        userAgent: String
    ): List<Pair<Places, WeatherResponse?>> {
        if (places.isEmpty()) return emptyList()

        val latitudes = places.joinToString(",") { it.lat }
        val longitudes = places.joinToString(",") { it.lon }

        //println("UserAgent(Weather): $userAgent")
        val responseText = try {
            client.get(ApiConfig.openMeteoBaseUrl) {
                parameter("latitude", latitudes)
                parameter("longitude", longitudes)
                parameter("current", currentFields)
                parameter("daily", dailyFields)
                parameter("timezone", "auto")
                accept(ContentType.Application.Json)
                header("User-Agent", userAgent)
            }.bodyAsText()
        } catch (_: Exception) {
            return places.map { it to null }
        }

        return try {
            val weatherResponses = if (responseText.trim().startsWith('[')) {
                json.decodeFromString<List<WeatherResponse>>(responseText)
            } else {
                listOf(json.decodeFromString<WeatherResponse>(responseText))
            }
            places.zip(weatherResponses).map { (place, weather) ->
                place to weather
            }
        } catch (_: Exception) {
            places.map { it to null }
        }
    }
}