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

import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.network.weather.DailyWeather
import kotlinx.serialization.Serializable

@Serializable
data class Places(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String,
    val osm_value: String,
    val city: String,
    val country: String,
    var currentWeather: CurrentWeather? = null,
    val dailyWeather: DailyWeather? = null,
)