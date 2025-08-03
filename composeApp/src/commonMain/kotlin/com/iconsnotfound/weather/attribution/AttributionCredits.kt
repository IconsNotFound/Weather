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

package com.iconsnotfound.weather.attribution

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.LinkText
import com.iconsnotfound.weather.lib.parseBoldTags
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.search_provider
import weather.composeapp.generated.resources.search_provider_ext
import weather.composeapp.generated.resources.weather_provider
import weather.composeapp.generated.resources.weather_provider_terms

@Composable
fun PhotonAttribution() {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinkText(
            string = stringResource(Res.string.search_provider),
            linkFontWeight = FontWeight.Bold,
            linkColor = MaterialTheme.colorScheme.primary
        )

        LinkText(
            string = stringResource(Res.string.search_provider_ext),
            linkFontWeight = FontWeight.Bold,
            linkColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun OpenMeteoAttribution() {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = parseBoldTags(stringResource(Res.string.weather_provider)),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        LinkText(
            string = stringResource(Res.string.weather_provider_terms),
            linkFontWeight = FontWeight.Bold,
            linkColor = MaterialTheme.colorScheme.outline,
            textColor = MaterialTheme.colorScheme.outline
        )
    }
}