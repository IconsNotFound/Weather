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

package com.iconsnotfound.weather.screens.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.iconsnotfound.weather.screens.settingsScreen.sections.BrandSection
import com.iconsnotfound.weather.screens.settingsScreen.sections.DonateSection
import com.iconsnotfound.weather.screens.settingsScreen.sections.PrivacyAndLicenceSection
import com.iconsnotfound.weather.screens.settingsScreen.sections.ThemeAppInfoSection
import com.iconsnotfound.weather.screens.settingsScreen.sections.TopBar
import kotlinx.coroutines.delay

class SettingsScreen() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SettingsScreenContent(
            onBack = { navigator.pop() },
        )
    }
}

@Composable
private fun SettingsScreenContent(onBack: () -> Unit){
    var showSections by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        delay(300)
        showSections = true
    }

    Column(
        modifier = Modifier.Companion.padding(vertical = 4.dp)
    ) {
        TopBar(onBack)
        if(showSections) {
            LazyColumn(
                state = listState,
                modifier = Modifier.Companion
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(4) { index ->
                    when(index) {
                        0 -> ThemeAppInfoSection()
                        1 -> PrivacyAndLicenceSection()
                        2 -> DonateSection()
                        else -> BrandSection()
                    }
                }
            }
        }
    }
}