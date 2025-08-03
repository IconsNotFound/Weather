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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.CardM3
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.CircularProgressBar
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.components.InfoItem1
import com.iconsnotfound.weather.components.ScrollableTextBox
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.lib.WeatherUtils
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.degree
import weather.composeapp.generated.resources.dew_point
import weather.composeapp.generated.resources.dew_point_round
import weather.composeapp.generated.resources.humidity
import weather.composeapp.generated.resources.humidity_round
import weather.composeapp.generated.resources.hyphen
import kotlin.math.round

@Composable
fun DewAndHumiditySection(currentWeather: CurrentWeather?) {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft(currentWeather) },
        sectionRight = { sectionRight(currentWeather) }
    )
}

@Composable
private fun sectionLeft(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    var dewPoint2 = hyphen
    currentWeather?.dew_point_2m?.let {
        dewPoint2 = round(it).toInt().toString()
    }
    InfoItem1(
        headerRowTitle = stringResource(Res.string.dew_point),
        headerRowIcon = Res.drawable.dew_point_round,
        bodyLeftTextUnit = stringResource(Res.string.degree),
        bodyLeftText = dewPoint2,
        bodyRightText = if(dewPoint2 != hyphen) WeatherUtils.dewPointDescription(dewPoint2.toDouble()) else dewPoint2,
        cardStyle = CardStyle.Elevated,
        cardContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 1.0f),
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun sectionRight(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    CardM3(
        style = CardStyle.Outlined,
        modifier = Modifier.padding(horizontal = 4.dp).fillMaxSize(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(32.dp)
    )
    {
        ImageViewM3(
            painter = painterResource(Res.drawable.humidity_round),
            style = ImageStyle.Icon,
            size = 24.dp,
            autoIconSize = false,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ScrollableTextBox(
            stringResource(Res.string.humidity),
            textStyle = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )

        var humidity = hyphen
        var humidityVal = 0.0
        currentWeather?.relative_humidity_2m?.let {
            humidity = round(it).toInt().toString()
            humidityVal = it/100
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressBar(
                humidityVal.toFloat(),
                textStyle = MaterialTheme.typography.displaySmall,
                unitStyle = MaterialTheme.typography.bodyMedium,
                sweepAngle = 300f,
                startAngle = -240f,
            )
        }
    }
}