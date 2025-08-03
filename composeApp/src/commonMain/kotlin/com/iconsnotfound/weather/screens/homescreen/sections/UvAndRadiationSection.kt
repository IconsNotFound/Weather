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
import com.iconsnotfound.weather.lib.WeatherUtils
import com.iconsnotfound.weather.network.weather.CurrentWeather
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.shortwave_radiation_round
import weather.composeapp.generated.resources.solar_radiation
import weather.composeapp.generated.resources.uv_index
import weather.composeapp.generated.resources.uv_index_round
import weather.composeapp.generated.resources.w_per_m_2
import kotlin.math.round

@Composable
fun UvAndRadiationSection(currentWeather: CurrentWeather?) {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft(currentWeather) },
        sectionRight = { sectionRight(currentWeather) }
    )
}

@Composable
private fun sectionLeft(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var precipitation = hyphen
    var precipitationVal = 0.0
    currentWeather?.uv_index?.let {
        precipitationVal = it
        precipitation = round(it).toInt().toString()

    }
    InfoItem1(
        headerRowTitle = stringResource(Res.string.uv_index),
        headerRowIcon = Res.drawable.uv_index_round,
        bodyLeftText = precipitation,
        bodyRightText = if(precipitation != hyphen) WeatherUtils.getUVRiskLevel(precipitationVal.toFloat()) else precipitation,
        cardStyle = CardStyle.Elevated,
        cardContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 1.0f),
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun sectionRight(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var airPressure2 = hyphen
    currentWeather?.shortwave_radiation?.let {
        airPressure2 = round(it).toInt().toString()
    }
    InfoItem1(
        headerRowTitle = stringResource(Res.string.solar_radiation),
        headerRowIcon = Res.drawable.shortwave_radiation_round,
        bodyLeftTextUnit = stringResource(Res.string.w_per_m_2),
        bodyLeftTextUnitWithVal = false,
        bodyLeftText = airPressure2,
        cardStyle = CardStyle.Outlined,
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    )
}