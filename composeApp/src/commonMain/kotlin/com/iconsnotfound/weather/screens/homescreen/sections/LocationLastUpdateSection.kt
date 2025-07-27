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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.components.ScrollableTextBox
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.lib.TimeUtils
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources._0
import weather.composeapp.generated.resources.am
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.last_updated
import weather.composeapp.generated.resources.location_rounded
import weather.composeapp.generated.resources.pm
import weather.composeapp.generated.resources.yesterday

@Composable
fun LocationLastUpdateSection(
    place: String,
    currentWeather: CurrentWeather?
) {
    Box (
        modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageViewM3(
                    painter = painterResource(Res.drawable.location_rounded),
                    style = ImageStyle.Icon,
                    size = 28.dp,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    autoIconSize = false,
                    modifier = Modifier.padding(end = 8.dp)
                )
                ScrollableTextBox(
                    text = place,
                    textStyle = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var lastUpdated = stringResource(Res.string.hyphen)
                currentWeather?.time?.let {
                    lastUpdated = TimeUtils.formatSmartTime(
                        input = it,
                        _0 = stringResource(Res.string._0),
                        am = stringResource(Res.string.am),
                        pm = stringResource(Res.string.pm),
                        yesterday = stringResource(Res.string.yesterday),
                    )
                    Text(
                        "${stringResource(Res.string.last_updated)} $lastUpdated",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}