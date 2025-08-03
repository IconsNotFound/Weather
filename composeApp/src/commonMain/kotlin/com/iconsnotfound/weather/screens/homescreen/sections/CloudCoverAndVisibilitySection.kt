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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.InfoItem1
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.lib.WeatherUtils
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.cloud_cover
import weather.composeapp.generated.resources.distance_visibility
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.km
import weather.composeapp.generated.resources.landscape_round
import kotlin.math.round

@Composable
fun CloudCoverAndVisibilitySection(currentWeather: CurrentWeather?) {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft(currentWeather) },
        sectionRight = { sectionRight(currentWeather) }
    )
}

@Composable
private fun sectionLeft(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var cloudCover = hyphen
    var cloudCoverVal = 0
    currentWeather?.cloud_cover?.let {
        cloudCoverVal = round(it).toInt()
        cloudCover = cloudCoverVal.toString()

    }
    InfoItem1(
        headerRowTitle = stringResource(Res.string.cloud_cover),
        headerRowIcon = WeatherUtils.interpretCloudCoverIcon(cloudCoverVal),
        bodyLeftText = cloudCover,
        bodyRightText = if(cloudCover != hyphen) WeatherUtils.interpretCloudCover(cloudCoverVal) else cloudCover,
        cardStyle = CardStyle.Outlined,
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun sectionRight(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var visibility = hyphen
    var visibilityVal = 0f
    currentWeather?.visibility?.let {
        visibilityVal = (it/1000.0).toFloat()
        visibility = round(visibilityVal).toInt().toString()
    }

    InfoItem1(
        headerRowTitle = stringResource(Res.string.distance_visibility),
        headerRowIcon = Res.drawable.landscape_round,
        bodyLeftTextUnit = stringResource(Res.string.km),
        bodyLeftTextUnitWithVal = false,
        bodyLeftText = visibility,
        bodyRightText = if(visibility != hyphen) WeatherUtils.getVisibilityDescription(visibilityVal) else visibility,
        cardStyle = CardStyle.Outlined,
        cardContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    )
}
