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

package com.iconsnotfound.weather.screens.settingsScreen.sections

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.LocalThemeManager
import com.iconsnotfound.weather.components.AppSettingsItem1
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.isSystemDefaultThemeDarkTheme
import com.iconsnotfound.weather.openUrl
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.donate
import weather.composeapp.generated.resources.donate_ext
import weather.composeapp.generated.resources.donate_round
import weather.composeapp.generated.resources.logo_patreon
import weather.composeapp.generated.resources.logo_patreon_dark
import weather.composeapp.generated.resources.url_patreon

@Composable
fun DonateSection() {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft() },
        sectionRight = { sectionRight() },
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(bottom = 8.dp)
    )
}

@Composable
private fun sectionLeft() {
    val patreonUrl = stringResource(Res.string.url_patreon)
    val themeManager = LocalThemeManager.current
    var isDarkTheme = when(themeManager.currentTheme) {
        AppDataStore.THEME.SYSTEM -> isSystemDefaultThemeDarkTheme()
        AppDataStore.THEME.LIGHT -> false
        else -> true
    }

    AppSettingsItem1(
        cardStyle = CardStyle.Filled,
        titleText = stringResource(Res.string.donate),
        subtitleText = stringResource(Res.string.donate_ext),
        icon = Res.drawable.donate_round,
        endIcon = if(!isDarkTheme) Res.drawable.logo_patreon else Res.drawable.logo_patreon_dark,
        endIconTint = null,
        itemOnClick = { openUrl(patreonUrl) }
    )
}
@Composable
private fun sectionRight() {}