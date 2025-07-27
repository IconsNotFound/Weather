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

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val current: CurrentWeather,
    val daily: DailyWeather
)

@Serializable
data class CurrentWeather(
    val time: String = "",
    val temperature_2m: Float = 0f,
    val weather_code: Int = 0,
    val wind_speed_10m: Float = 0f,
    val apparent_temperature: Double = 0.0,
    val relative_humidity_2m: Double = 0.0,
    val wind_direction_10m: Double = 0.0,
    val surface_pressure: Double = 0.0,
    val visibility: Double = 0.0,
    val uv_index: Double = 0.0,
    val precipitation: Double = 0.0,
    val dew_point_2m: Double = 0.0,
    val cloud_cover: Double = 0.0,
    val wind_gusts_10m: Double = 0.0,
    val shortwave_radiation: Double = 0.0,
)

@Serializable
data class DailyWeather(
    val sunrise: List<String> = emptyList(),
    val sunset: List<String> = emptyList()
)