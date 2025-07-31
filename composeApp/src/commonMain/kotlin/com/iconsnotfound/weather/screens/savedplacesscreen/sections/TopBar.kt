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

package com.iconsnotfound.weather.screens.savedplacesscreen.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.ButtonM3
import com.iconsnotfound.weather.components.ButtonStyle
import com.iconsnotfound.weather.components.IconButtonM3
import com.iconsnotfound.weather.components.IconButtonStyle
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.add_place
import weather.composeapp.generated.resources.places

@Composable
fun TopBar(
    savedLocationShowing: MutableState<Boolean>,
    onLocationSearchClick: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButtonM3(
            onClick = { onBack() },
            icon = Icons.Default.ArrowBackIosNew,
            style = IconButtonStyle.Text,
        )
        Text(
            text = stringResource(Res.string.places),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        if(savedLocationShowing.value) {
            ButtonM3(
                text = stringResource(Res.string.add_place),
                onClick = { onLocationSearchClick() },
                icon = Icons.Default.Add,
                style = ButtonStyle.Outlined,
            )
        }
    }
}