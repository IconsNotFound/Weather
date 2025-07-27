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
import com.iconsnotfound.weather.components.InfoItem2
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.lib.WeatherUtils
import com.iconsnotfound.weather.lib.formatPrecipitation
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.air_pressure
import weather.composeapp.generated.resources.air_pressure_round2
import weather.composeapp.generated.resources.hpa
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.mm
import weather.composeapp.generated.resources.precipitation
import weather.composeapp.generated.resources.precipitation_round
import kotlin.math.round

@Composable
fun PrecipitationSection(currentWeather: CurrentWeather?) {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft(currentWeather) },
        sectionRight = { sectionRight(currentWeather) }
    )
}

@Composable
private fun sectionLeft(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var precipitation = hyphen
    currentWeather?.precipitation?.let {
        precipitation = formatPrecipitation(it)
    }
    InfoItem1(
        headerRowTitle = stringResource(Res.string.precipitation),
        headerRowIcon = Res.drawable.precipitation_round,
        bodyLeftTextUnit = stringResource(Res.string.mm),
        bodyLeftTextUnitWithVal = false,
        bodyLeftText = precipitation,
        bodyRightText = if(precipitation != hyphen) WeatherUtils.describePrecipitation(precipitation.toDouble()) else precipitation,
        cardStyle = CardStyle.Outlined,
        cardContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer)
    )
}

@Composable
private fun sectionRight(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var airPressure2 = hyphen
    currentWeather?.surface_pressure?.let {
        airPressure2 = round(it).toInt().toString()
    }

    InfoItem2(
        headerRowTitle = stringResource(Res.string.air_pressure),
        headerRowIcon = Res.drawable.air_pressure_round2,
        bodyLeftTextUnit = stringResource(Res.string.hpa),
        bodyLeftTextUnitWithVal = false,
        bodyLeftText = airPressure2,
        cardStyle = CardStyle.Outlined,
        cardContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer)
    )
}

