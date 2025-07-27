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

package com.iconsnotfound.weather.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.iconsnotfound.weather.SettingsHolder
import com.iconsnotfound.weather.attribution.OpenMeteoAttribution
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.config.AppInfo
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.data.WeatherRepository
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.network.weather.DailyWeather
import com.iconsnotfound.weather.screens.homescreen.sections.CloudCoverAndVisibilitySection
import com.iconsnotfound.weather.screens.homescreen.sections.DewAndHumiditySection
import com.iconsnotfound.weather.screens.homescreen.sections.ForecastSection
import com.iconsnotfound.weather.screens.homescreen.sections.LocationLastUpdateSection
import com.iconsnotfound.weather.screens.homescreen.sections.PrecipitationSection
import com.iconsnotfound.weather.screens.homescreen.sections.SunSection
import com.iconsnotfound.weather.screens.homescreen.sections.TopBar
import com.iconsnotfound.weather.screens.homescreen.sections.UvAndRadiationSection
import com.iconsnotfound.weather.screens.homescreen.sections.WindSection
import com.iconsnotfound.weather.screens.placesearchscreen.LocationSearchScreen
import com.iconsnotfound.weather.screens.savedplacesscreen.SavedPlacesScreen
import com.iconsnotfound.weather.screens.settingsScreen.SettingsScreen
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.add_place
import weather.composeapp.generated.resources.no_default_place_set
import weather.composeapp.generated.resources.select_a_place
import weather.composeapp.generated.resources.weather_updated

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        HomeScreenContent(
            onSettingsClick = { navigator.push(SettingsScreen()) },
            onSavedPlacesClick = { navigator.push(SavedPlacesScreen()) },
            onLocationSearchClick = { isExpanded ->
                if (isExpanded) navigator.push(LocationSearchScreen())
            }
        )
    }
}

@Composable
private fun HomeScreenContent(
    onSettingsClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    onLocationSearchClick: (Boolean) -> Unit,
) {
    val listState = rememberLazyListState()
    val settings = SettingsHolder.settings
    val msgNoDefPlace = stringResource(Res.string.no_default_place_set)
    val msgSelAPlace = stringResource(Res.string.select_a_place)
    val msgAddPlace = stringResource(Res.string.add_place)
    val weatherUpdated = stringResource(Res.string.weather_updated)
    val userAgentString = AppInfo.userAgent()

    val coroutineScope = rememberCoroutineScope()
    var currentWeather by remember { mutableStateOf<CurrentWeather?>(null) }
    var dailyWeather by remember { mutableStateOf<DailyWeather?>(null) }

    var count by remember { mutableIntStateOf(0) }
    var place by remember { mutableStateOf("") }
    var showSections by remember { mutableStateOf(false) }
    var isLoading = remember { mutableStateOf(false) }
    var hasDefaultLocation by remember { mutableStateOf(true) }
    var totalSavedLocations by remember { mutableStateOf(0) }

    val weatherRepository = remember {
        WeatherRepository(
            initialPlaces = AppDataStore.loadAll(settings),
            settings = settings,
            coroutineScope = coroutineScope
        )
    }
    remember { weatherRepository.places }
    val updated = weatherRepository.dataUpdated


    LaunchedEffect(Unit) {
        isLoading.value = true
        delay(300)
        totalSavedLocations = AppDataStore.getTotalSavedPlaces(settings)
        val defaultLocation = AppDataStore.getDefaultPlace(settings)
        if(defaultLocation == null) {
            hasDefaultLocation = false
            isLoading.value = false
            showSections = true
        }
        defaultLocation?.name?.let { place = it }
        weatherRepository.refreshWeather(msgSuccess = weatherUpdated, userAgentString = userAgentString)

        defaultLocation?.let { location ->
            location.currentWeather?.let { currentWeather = it }
            location.dailyWeather?.let { dailyWeather = it }
            if(currentWeather != null || dailyWeather != null) {
                isLoading.value = false
                showSections = true
            }
        }
    }

    LaunchedEffect(updated) {
        if(updated) {

            count++
            weatherRepository.resetDataUpdatedFlag()
            val defaultLocation = AppDataStore.getDefaultPlace(settings)
            defaultLocation?.let { location ->
                location.currentWeather?.let { currentWeather = it }
                location.dailyWeather?.let { dailyWeather = it }
                isLoading.value = false
                showSections = true
            }
        }
    }

    Column {
        TopBar(onSettingsClick, onSavedPlacesClick, weatherRepository, isLoading)
        if (isLoading.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        if(showSections) {
            if(hasDefaultLocation) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    items(9) { section ->
                        when (section) {
                            0 -> ForecastSection(currentWeather, dailyWeather)
                            1 -> LocationLastUpdateSection(place, currentWeather)
                            2 -> DewAndHumiditySection(currentWeather)
                            3 -> WindSection(currentWeather)
                            4 -> PrecipitationSection(currentWeather)
                            5 -> UvAndRadiationSection(currentWeather)
                            6 -> CloudCoverAndVisibilitySection(currentWeather)
                            7 -> SunSection(dailyWeather)
                            else -> OpenMeteoAttribution()
                        }
                    }
                }
            }
            else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = msgNoDefPlace,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if(totalSavedLocations>0) {
                        ButtonM3(
                            onClick = { onSavedPlacesClick() },
                            icon = Icons.Default.LocationCity,
                            style = ButtonStyle.Filled,
                            text = msgSelAPlace
                        )
                    }
                    else {
                        ButtonM3(
                            text = msgAddPlace,
                            onClick = { onLocationSearchClick(true) },
                            icon = Icons.Default.Add,
                            style = ButtonStyle.Filled,
                        )
                    }
                }
            }
        }
    }
}