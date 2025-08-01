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

package com.iconsnotfound.weather.screens.placesearchscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.iconsnotfound.weather.BackHandler
import com.iconsnotfound.weather.SettingsHolder
import com.iconsnotfound.weather.network.places.PlacesModels
import com.iconsnotfound.weather.screens.placesearchscreen.sections.ResultSection
import com.iconsnotfound.weather.screens.placesearchscreen.sections.SearchSection
import com.iconsnotfound.weather.screens.placesearchscreen.sections.TopBar
import kotlinx.coroutines.Job

class LocationSearchScreen(private val fromHomeScreen: Boolean = false): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        LocationSearchScreenContent(
            onBack = { navigator.pop() },
            fromHomeScreen = fromHomeScreen
        )
    }
}

@Composable
private fun LocationSearchScreenContent(
    onBack: (Boolean) -> Unit,
    fromHomeScreen: Boolean
) {
    val offsetX = remember { Animatable(-1000f) }
    var showList by remember { mutableStateOf(false) }
    var query = remember { mutableStateOf("") }
    var results = remember { mutableStateOf<List<PlacesModels>>(emptyList()) }
    var isLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob = remember { mutableStateOf<Job?>(null) }
    val settings = SettingsHolder.settings
    val listState = rememberLazyListState()
    BackHandler {
        onBack(fromHomeScreen)
    }
    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, animationSpec = tween(500))
        showList = true
    }

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        TopBar(onBack = onBack, fromHomeScreen = fromHomeScreen)

        if (isLoading.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        if(showList) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(2) { section ->
                    when(section) {
                        0 -> SearchSection(
                            query = query,
                            searchJob = searchJob,
                            coroutineScope = coroutineScope,
                            isLoading = isLoading,
                            results = results,
                        )
                        1 -> ResultSection(
                            results = results,
                            onBack = onBack,
                            settings = settings,
                            fromHomeScreen = fromHomeScreen
                        )
                    }
                }
            }
        }
    }
}
