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

package com.iconsnotfound.weather.screens.savedplacesscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.iconsnotfound.weather.BackHandler
import com.iconsnotfound.weather.SettingsHolder
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.components.InfoItem3
import com.iconsnotfound.weather.components.SavedPlaceContextMenu
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.data.Places
import com.iconsnotfound.weather.screens.placesearchscreen.LocationSearchScreen
import com.iconsnotfound.weather.screens.savedplacesscreen.sections.TopBar
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.add_place
import weather.composeapp.generated.resources.default_place_updated
import weather.composeapp.generated.resources.no_saved_places
import weather.composeapp.generated.resources.place_deleted

class SavedPlacesScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SavedPlacesScreenContent (
            onLocationSearchClick = { navigator.push(LocationSearchScreen()) },
            onBack = { navigator.pop() }
        )
    }
}

@Composable
fun SavedPlacesScreenContent(
    onLocationSearchClick: () -> Unit,
    onBack: () -> Unit
) {
    val offsetX = remember { Animatable(-1000f) }
    val listState = rememberLazyListState()
    val settings = SettingsHolder.settings
    val msgSuccess = stringResource(Res.string.place_deleted)
    val defaultPlaceUpdated = stringResource(Res.string.default_place_updated)

    var results = remember { mutableStateListOf<Places>() }
    val chunkSize = 5

    var isLoading by remember { mutableStateOf(false) }
    val defaultLocation = remember { mutableStateOf<Places?>(null) }
    var totalSavedLocations by remember { mutableStateOf(0) }
    var showSections = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, animationSpec = tween(500))
        isLoading = true
        totalSavedLocations = AppDataStore.getTotalSavedPlaces(settings)
        if(totalSavedLocations == 0) {
            isLoading = false
            showSections.value = false
            return@LaunchedEffect
        }
        defaultLocation.value = AppDataStore.getDefaultPlace(settings)
        val all = AppDataStore.loadAll(settings)
        val chunks = all.chunked(chunkSize)
        for (chunk in chunks) {
            delay(300)
            results.addAll(chunk)
            isLoading = false
        }
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        TopBar(showSections, onLocationSearchClick, onBack)
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if(showSections.value) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                item {
                    results.forEach { location ->
                        InfoItem3(
                            location = location,
                            headerButton = {
                                SavedPlaceContextMenu(
                                    onDelete = {
                                        val isDefaultLocation = AppDataStore.isDefaultPlace(location.id, settings)
                                        AppDataStore.delete(location.id, settings, msgSuccess)
                                        results.removeAll() {it.id == location.id }
                                        if (defaultLocation.value?.id == location.id) {
                                            defaultLocation.value = null
                                        }
                                        if(isDefaultLocation) {
                                            AppDataStore.clearDefaultPlace(settings)
                                        }
                                        totalSavedLocations = AppDataStore.getTotalSavedPlaces(settings)
                                        showSections.value = totalSavedLocations != 0
                                    },
                                )
                            },
                            infoItemOnClick = {
                                AppDataStore.setDefaultPlace(location.id, settings, msgSuccess = defaultPlaceUpdated)
                                defaultLocation.value = location
                                onBack()
                            },
                            isDefault = defaultLocation.value?.id == location.id
                        )
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
                    text = stringResource(Res.string.no_saved_places),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ButtonM3(
                    text = stringResource(Res.string.add_place),
                    onClick = { onLocationSearchClick() },
                    icon = Icons.Default.Add,
                    style = ButtonStyle.Filled,
                )
            }
        }
    }
}