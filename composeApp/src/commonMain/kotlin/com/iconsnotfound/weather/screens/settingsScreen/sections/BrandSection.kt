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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.LocalThemeManager
import com.iconsnotfound.weather.components.IconButtonM3
import com.iconsnotfound.weather.components.IconButtonStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.isSystemDefaultThemeDarkTheme
import com.iconsnotfound.weather.openUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.brand_name
import weather.composeapp.generated.resources.from
import weather.composeapp.generated.resources.iconsnotfound_logo
import weather.composeapp.generated.resources.logo_github
import weather.composeapp.generated.resources.logo_github_dark
import weather.composeapp.generated.resources.logo_youtube
import weather.composeapp.generated.resources.url_github
import weather.composeapp.generated.resources.url_youtube

@Composable
fun BrandSection() {
    val themeManager = LocalThemeManager.current
    var isDarkTheme = when(themeManager.currentTheme) {
        AppDataStore.THEME.SYSTEM -> isSystemDefaultThemeDarkTheme()
        AppDataStore.THEME.LIGHT -> false
        else -> true
    }

    val githubUrl = stringResource(Res.string.url_github)
    val youtubeUrl = stringResource(Res.string.url_youtube)
    val brandName = stringResource(Res.string.brand_name)

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(Res.string.from),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ImageViewM3(
            painter = painterResource(Res.drawable.iconsnotfound_logo),
            size = 48.dp,
            autoIconSize = false,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = brandName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row{
            IconButtonM3(
                painter = painterResource(Res.drawable.logo_youtube),
                style = IconButtonStyle.Image,
                onClick = { openUrl(youtubeUrl) },
            )
            Spacer(modifier = Modifier.width(32.dp))
            IconButtonM3(
                painter = painterResource(if(isDarkTheme) Res.drawable.logo_github_dark else Res.drawable.logo_github),
                style = IconButtonStyle.Image,
                onClick = { openUrl(githubUrl) },
            )
        }
    }
}