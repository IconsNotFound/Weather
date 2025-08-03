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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.LocalThemeManager
import com.iconsnotfound.weather.SettingsHolder.settings
import com.iconsnotfound.weather.components.AppSettingsItem1
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.CustomDialog
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.data.AppDataStore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.app_info
import weather.composeapp.generated.resources.app_name
import weather.composeapp.generated.resources.app_version
import weather.composeapp.generated.resources.close
import weather.composeapp.generated.resources.info_round
import weather.composeapp.generated.resources.mainly_clear_night_rounded
import weather.composeapp.generated.resources.theme
import weather.composeapp.generated.resources.theme_dark
import weather.composeapp.generated.resources.theme_light
import weather.composeapp.generated.resources.theme_round
import weather.composeapp.generated.resources.theme_system
import weather.composeapp.generated.resources.weather_app_logo

@Composable
fun ThemeAppInfoSection() {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft() },
        sectionRight = { sectionRight() },
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(bottom = 8.dp)
    )
}

@Composable
private fun sectionLeft() {
    val themeManager = LocalThemeManager.current

    var showDialog by remember { mutableStateOf(false) }
    val options = listOf<Pair<String, Int>>(
        Pair(stringResource(Res.string.theme_system), AppDataStore.THEME.SYSTEM),
        Pair(stringResource(Res.string.theme_light), AppDataStore.THEME.LIGHT),
        Pair(stringResource(Res.string.theme_dark), AppDataStore.THEME.DARK),
    )
    var selectedValue by remember { mutableStateOf(
        AppDataStore.getTheme(settings)
    ) }

    AppSettingsItem1(
        cardStyle = CardStyle.Filled,
        titleText = stringResource(Res.string.theme),
        subtitleText = options.first{ it.second == selectedValue }.first,
        icon = Res.drawable.theme_round,
        itemOnClick = { showDialog = true }
    )

    CustomDialog(
        isVisible = showDialog,
        onDismiss = { showDialog = false }
    ) {
        Column(Modifier.padding(24.dp).fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                ImageViewM3(
                    painter = painterResource(Res.drawable.mainly_clear_night_rounded),
                    style = ImageStyle.Icon,
                    size = 24.dp,
                    autoIconSize = false
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(Res.string.theme),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Column {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedValue = option.second
                                themeManager.setTheme(option.second)
                                showDialog = false
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (option.second == selectedValue),
                            onClick = {
                                selectedValue = option.second
                                AppDataStore.setTheme(settings, option.second)
                                showDialog = false
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(option.first)
                    }
                }
            }
        }
    }
}

@Composable
private fun sectionRight() {
    val appVersion = stringResource(Res.string.app_version)
    val close = stringResource(Res.string.close)
    val appInfo = stringResource(Res.string.app_info)
    var showDialog by remember { mutableStateOf(false) }
    AppSettingsItem1(
        cardStyle = CardStyle.Outlined,
        titleText = appInfo,
        subtitleText = appVersion,
        icon = Res.drawable.info_round,
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        itemOnClick = { showDialog = true }
    )

    CustomDialog(
        isVisible = showDialog,
        onDismiss = { showDialog = false }
    ) {
        Column(Modifier.padding(24.dp).fillMaxWidth()) {

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = appVersion,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )

            ImageViewM3(
                painter = painterResource(Res.drawable.weather_app_logo),
                contentColor = null,
                size = 192.dp
            )

            Spacer(Modifier.height(16.dp))
            ButtonM3(
                text = close,
                modifier = Modifier.align(Alignment.End),
                onClick = { showDialog = false },
                style = ButtonStyle.Outlined
            )
        }
    }
}