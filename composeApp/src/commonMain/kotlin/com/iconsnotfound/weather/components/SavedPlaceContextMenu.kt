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

package com.iconsnotfound.weather.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.delete
import weather.composeapp.generated.resources.more_vert_round

@Composable
fun SavedPlaceContextMenu(
    headerButtonIcon: Painter = painterResource(Res.drawable.more_vert_round),
    headerButtonIconSize: Dp = 24.dp,
    headerButtonStyle: IconButtonStyle = IconButtonStyle.Text,
    onDelete: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Box{
        Box {
            IconButtonM3(
                onClick = { expanded = true },
                painter = headerButtonIcon,
                iconSize = headerButtonIconSize,
                style = headerButtonStyle,
                buttonSize = headerButtonIconSize+8.dp,
                contentColor = MaterialTheme.colorScheme.primary
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onDelete?.invoke()
                    },
                    text = { Text(stringResource(Res.string.delete)) }
                )
            }
        }
    }
}