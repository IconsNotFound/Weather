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

package com.iconsnotfound.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.iconsnotfound.weather.components.ClickAwayBox
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.screens.homescreen.HomeScreen
import com.iconsnotfound.weather.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalThemeManager = staticCompositionLocalOf<ThemeManager> {
    error("No ThemeManager provided!")
}

@Composable
@Preview
fun App() {
    val themeManager = remember { ThemeManager() }
    LaunchedEffect(Unit) {
        themeManager.loadInitialTheme()
    }

    CompositionLocalProvider(
        LocalThemeManager provides themeManager
    ) {
        AppTheme(darkTheme = when (themeManager.currentTheme) {
            AppDataStore.THEME.SYSTEM -> isSystemDefaultThemeDarkTheme()
            AppDataStore.THEME.LIGHT -> false
            AppDataStore.THEME.DARK -> true
            else -> false
        }) {
            ClickAwayBox {
                Column(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .widthIn(min = 360.dp, max = 800.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Navigator(HomeScreen()) { navigator ->
                        SlideTransition(navigator)
                    }
                }
            }
        }
    }
}