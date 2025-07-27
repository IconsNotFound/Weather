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

package com.iconsnotfound.weather.screens.homescreen.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.network.weather.DailyWeather
import com.iconsnotfound.weather.lib.WeatherUtils
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.degree
import weather.composeapp.generated.resources.feels_like
import weather.composeapp.generated.resources.hyphen
import kotlin.math.round

@Composable
fun ForecastSection(currentWeather: CurrentWeather?, dailyWeather: DailyWeather?) {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft(currentWeather) },
        sectionRight = { sectionRight(currentWeather, dailyWeather) },
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(top = 64.dp)
    )
}

@Composable
private fun sectionLeft(currentWeather: CurrentWeather?){
    val hyphen = stringResource(Res.string.hyphen)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        var temperature = hyphen
        currentWeather?.temperature_2m?.let {
            temperature = round(it).toInt().toString()
        }
        Text(
            "${temperature}${stringResource(Res.string.degree)}",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var feelsLike = hyphen
        currentWeather?.apparent_temperature?.let {
            feelsLike = round(it).toInt().toString()
        }

        Text(
            "${stringResource(Res.string.feels_like)} ${feelsLike}${stringResource(Res.string.degree)}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun sectionRight(currentWeather: CurrentWeather?, dailyWeather: DailyWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var weatherDescription = hyphen
    currentWeather?.weather_code?.let { weatherCode ->
        weatherDescription = WeatherUtils.getWeatherDescription(weatherCode)

        var sunriseIso: String? = null
        var sunsetIso: String? = null
        dailyWeather?.let { daily ->
            sunriseIso = daily.sunrise.first()
            sunsetIso = daily.sunset.first()
        }

        val painter = painterResource(WeatherUtils.getWeatherIcon(weatherCode, sunriseIso, sunsetIso))
        ImageViewM3(
            painter = painter,
            style = ImageStyle.Icon,
            size = 48.dp,
            autoIconSize = false,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
    Text(
        text = weatherDescription,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    var cloudCover = hyphen
    currentWeather?.cloud_cover?.let {
        cloudCover = WeatherUtils.interpretCloudCover(round(it).toInt())
    }
}