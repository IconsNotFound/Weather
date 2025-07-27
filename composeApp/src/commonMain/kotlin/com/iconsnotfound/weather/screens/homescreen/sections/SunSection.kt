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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.CardM3
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.HorizontalProgressBar
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.network.weather.DailyWeather
import com.iconsnotfound.weather.lib.TimeUtils
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources._0
import weather.composeapp.generated.resources._06_00
import weather.composeapp.generated.resources.am
import weather.composeapp.generated.resources.clear_sky_day_rounded
import weather.composeapp.generated.resources.clear_sky_night_rounded
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.pm
import weather.composeapp.generated.resources.sunrise
import weather.composeapp.generated.resources.sunrise_round
import weather.composeapp.generated.resources.sunset
import weather.composeapp.generated.resources.sunset_round

@Composable
fun SunSection(dailyWeather: DailyWeather?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        var sunrise = stringResource(Res.string.hyphen)
        var sunset = stringResource(Res.string.hyphen)
        var currentPosition = 0f
        var isNowNight = false

        dailyWeather?.let { daily ->
            var (a, b, c) = TimeUtils.sunsetSunriseTime(
                daily.sunrise,
                daily.sunset,
                _06_00 = stringResource(Res.string._06_00),
                am = stringResource(Res.string.am),
                pm = stringResource(Res.string.pm),
                _0 = stringResource(Res.string._0)
            )
            sunrise = a
            sunset = b
            currentPosition = c.first
            isNowNight = c.second
        }

        CardM3(
            style = CardStyle.Outlined,
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            shape = RoundedCornerShape(32.dp)
        ){
            HorizontalProgressBar(
                progress = currentPosition,
                startIcon = painterResource(Res.drawable.sunrise_round),
                endIcon = painterResource(Res.drawable.sunset_round),
                startText = sunrise,
                endText = sunset,
                startLabel = stringResource(Res.string.sunrise),
                endLabel = stringResource(Res.string.sunset),
                progressTipIcon = painterResource(if(isNowNight) Res.drawable.clear_sky_night_rounded else Res.drawable.clear_sky_day_rounded),
                progressBarHeight = 12.dp,
                progressTipStyle = ImageStyle.Filled
            )
        }
    }
}