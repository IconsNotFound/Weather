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

package com.iconsnotfound.weather.screens.licencescreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.iconsnotfound.weather.BackHandler
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.CustomDialog
import com.iconsnotfound.weather.components.InfoItem4
import com.iconsnotfound.weather.openUrl
import com.iconsnotfound.weather.screens.licencescreen.sections.TopBar
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.close
import weather.composeapp.generated.resources.url_freepik
import weather.composeapp.generated.resources.url_open_meteo_license
import weather.composeapp.generated.resources.url_openstreetmap
import weather.composeapp.generated.resources.url_svg_repo_cc0

class LicenceScreen(): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        LicenceScreenContent(
            onBack = { navigator.pop() }
        )
    }
}

@Composable
private fun LicenceScreenContent(onBack: () -> Unit) {
    val offsetX = remember { Animatable(-1000f) }
    val listState = rememberLazyListState()
    var isReady by remember { mutableStateOf(false) }
    var readFile by remember { mutableStateOf(false) }
    var strVal = remember { mutableStateOf("") }
    var clickedItemTitle = remember { mutableStateOf("") }
    var fileName = remember { mutableStateOf("") }
    var showList by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    val openMeteoUrl = stringResource(Res.string.url_open_meteo_license)
    val osmUrl = stringResource(Res.string.url_openstreetmap)
    val freepikUrl = stringResource(Res.string.url_freepik)
    val svgRepoUrl = stringResource(Res.string.url_svg_repo_cc0)

    val list = listOf<Pair<Triple<String, String, String>, Int>>(
        Pair(Triple(LicenceFiles.WEATHER_TITLE, LicenceFiles.WEATHER_SUBTITLE, LicenceFiles.WEATHER), 0),
        Pair(Triple(LicenceFiles.PHOTON_TITLE, LicenceFiles.PHOTON_SUBTITLE, LicenceFiles.PHOTON), 1),
        Pair(Triple(LicenceFiles.OSM_TITLE, LicenceFiles.OSM_SUBTITLE, LicenceFiles.OSM), 2),
        Pair(Triple(LicenceFiles.OPEN_METEO_TITLE, LicenceFiles.OPEN_METEO_SUBTITLE, LicenceFiles.OPEN_METEO), 2),
        Pair(Triple(LicenceFiles.ANDROIDX_TITLE, LicenceFiles.ANDROIDX_SUBTITLE, LicenceFiles.ANDROIDX), 1),
        Pair(Triple(LicenceFiles.COMPOSE_HOT_RELOAD_TITLE, LicenceFiles.COMPOSE_HOT_RELOAD_SUBTITLE, LicenceFiles.COMPOSE_HOT_RELOAD), 1),
        Pair(Triple(LicenceFiles.KOTLIN_TITLE, LicenceFiles.KOTLIN_SUBTITLE, LicenceFiles.KOTLIN), 1),
        Pair(Triple(LicenceFiles.JUNIT_TITLE, LicenceFiles.JUNIT_SUBTITLE, LicenceFiles.JUNIT), 1),
        Pair(Triple(LicenceFiles.KTOR_TITLE, LicenceFiles.KTOR_SUBTITLE, LicenceFiles.KTOR), 1),
        Pair(Triple(LicenceFiles.MATERIAL_DESIGN_TITLE, LicenceFiles.MATERIAL_DESIGN_SUBTITLE, LicenceFiles.MATERIAL_DESIGN), 1),
        Pair(Triple(LicenceFiles.MULTIPLATFORM_SETTINGS_TITLE, LicenceFiles.MULTIPLATFORM_SETTINGS_SUBTITLE, LicenceFiles.MULTIPLATFORM_SETTINGS), 1),
        Pair(Triple(LicenceFiles.VOYAGER_TITLE, LicenceFiles.VOYAGER_SUBTITLE, LicenceFiles.VOYAGER), 1),
        Pair(Triple(LicenceFiles.FREEPIK_TITLE, LicenceFiles.FREEPIK_SUBTITLE, LicenceFiles.FREEPIK), 2),
        Pair(Triple(LicenceFiles.WIND_MILL_TITLE, LicenceFiles.WIND_MILL_SUBTITLE, LicenceFiles.WIND_MILL), 2),
    )

    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, animationSpec = tween(500))
        showList = true
    }

    if(readFile) {
        LaunchedEffect(Unit) {
            strVal.value = readLicenseText(fileName.value)
            isReady = true
            readFile = false
            fileName.value = ""
        }
    }

    Column(
        modifier = Modifier.Companion.padding(vertical = 4.dp)
    ) {
        TopBar(onBack)
        if(showList) {
            LazyColumn(
                state = listState,
                modifier = Modifier.Companion
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            ) {
                list.forEach { component ->
                    item {
                        InfoItem4(
                            title = component.first.first,
                            subTitle = component.first.second,
                            infoItemOnClick = {
                                if(component.second != 2) {
                                    fileName.value = component.first.third
                                    clickedItemTitle.value = component.first.first
                                    readFile = true
                                }
                                else{
                                    when(component.first.first) {
                                        LicenceFiles.OPEN_METEO_TITLE -> openUrl(openMeteoUrl)
                                        LicenceFiles.OSM_TITLE -> openUrl(osmUrl)
                                        LicenceFiles.FREEPIK_TITLE -> openUrl(freepikUrl)
                                        LicenceFiles.WIND_MILL_TITLE -> openUrl(svgRepoUrl)
                                    }
                                }
                            },
                            showEndIcon = component.second == 2,
                            cardStyle = if(component.second == 0) CardStyle.Outlined else CardStyle.Filled
                        )
                    }
                }
            }
        }
    }

    CustomDialog(
        isVisible = isReady,
        onDismiss = { isReady = false }
    ) {
        Column(Modifier.padding(24.dp).fillMaxWidth()) {
            Text(
                text = clickedItemTitle.value,
                style = MaterialTheme.typography.headlineSmall
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Text(
                text = strVal.value,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, fill = false)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))
            ButtonM3(
                text = stringResource(Res.string.close),
                modifier = Modifier.align(Alignment.End),
                onClick = { isReady = false },
                style = ButtonStyle.Outlined
            )
        }
    }
}

suspend fun readLicenseText(resourcePath: String): String {
    val bytes = Res.readBytes(resourcePath)
    return bytes.decodeToString()
}