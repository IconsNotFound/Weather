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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.SettingsHolder.settings
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.components.IconButtonM3
import com.iconsnotfound.weather.components.IconButtonStyle
import com.iconsnotfound.weather.config.AppInfo
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.data.WeatherRepository
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.places
import weather.composeapp.generated.resources.weather_updated

@Composable
fun TopBar(
    onSettingsClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    weatherRepository: WeatherRepository,
    isLoading: MutableState<Boolean>
) {
    val weatherUpdated = stringResource(Res.string.weather_updated)
    val userAgentString = AppInfo.userAgent()
    val isRefreshVisible by remember { mutableStateOf(
        AppDataStore.getTotalSavedPlaces(settings)>0 && AppDataStore.getDefaultPlace(settings) != null
    ) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if(isRefreshVisible) {
            IconButtonM3(
                onClick = {
                    isLoading.value = true
                    weatherRepository.refreshWeather(manualRefresh = true, msgSuccess = weatherUpdated, userAgentString = userAgentString)
                },
                icon = Icons.Default.Refresh,
                style = IconButtonStyle.Text,
            )
            ButtonM3(
                onClick = { onSavedPlacesClick() },
                icon = Icons.Default.LocationCity,
                style = ButtonStyle.Outlined,
                text = stringResource(Res.string.places)
            )
        }
        IconButtonM3(
            onClick = { onSettingsClick() },
            icon = Icons.Default.Settings,
            style = IconButtonStyle.Text,
        )
    }
}