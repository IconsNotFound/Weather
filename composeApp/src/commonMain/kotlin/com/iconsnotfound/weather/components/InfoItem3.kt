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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.data.Places
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.comma_space
import weather.composeapp.generated.resources.location_checked_round

@Composable
fun InfoItem3(
    location: Places,
    infoItemOnClick: (() -> Unit)? = null,
    headerButton: (@Composable () -> Unit)? = null,
    isDefault: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    cardStyle: CardStyle = CardStyle.Filled,
    isDefaultIcon: Painter = painterResource(Res.drawable.location_checked_round),
    cardBorder: BorderStroke? = if (cardStyle == CardStyle.Outlined)
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    else null,
) {
    val commaSpace = stringResource(Res.string.comma_space)
    CardM3(
        style = cardStyle,
        modifier = Modifier.fillMaxWidth().padding(paddingValues),
        onClick = infoItemOnClick,
        contentPadding = PaddingValues(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = cardBorder,
    ) {
        Box(
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        location.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    val subtitle = "${location.osm_value}${if (location.city!="") commaSpace else ""}${location.city}${if (location.country!="") commaSpace else ""}${location.country}"
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(isDefault) {
                        ImageViewM3(
                            painter = isDefaultIcon,
                            size = 24.dp,
                            autoIconSize = false,
                            modifier = Modifier.padding(end = if(headerButton!=null)8.dp else 0.dp)
                        )
                    }
                    headerButton?.invoke()
                }
            }
        }
    }
}