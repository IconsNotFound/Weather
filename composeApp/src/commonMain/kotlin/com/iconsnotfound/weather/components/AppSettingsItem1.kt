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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppSettingsItem1(
    cardStyle: CardStyle = CardStyle.Outlined,
    cardModifier: Modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
    cardContainerColor: Color = when (cardStyle) {
        CardStyle.Filled -> MaterialTheme.colorScheme.secondaryContainer
        CardStyle.Elevated-> MaterialTheme.colorScheme.surface
        CardStyle.Outlined -> Color.Transparent
    },
    cardContentColor: Color = when (cardStyle) {
        CardStyle.Filled -> MaterialTheme.colorScheme.onPrimaryContainer
        CardStyle.Elevated -> MaterialTheme.colorScheme.onSurface
        CardStyle.Outlined -> MaterialTheme.colorScheme.onSurface
    },
    cardBorder: BorderStroke? = if (cardStyle == CardStyle.Outlined)
        BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outline)
    else null,
    icon: DrawableResource? = null,
    iconStyle: ImageStyle = ImageStyle.Icon,
    iconSize: Dp = 24.dp,
    autoIconSize: Boolean = false,
    endIcon: DrawableResource? = null,
    endIconStyle: ImageStyle = ImageStyle.Icon,
    endIconSize: Dp = 20.dp,
    autoEndIconSize: Boolean = false,
    endIconTint: Color? = MaterialTheme.colorScheme.secondary,
    subtitleText: String? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    titleTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    subtitleTextColor: Color = MaterialTheme.colorScheme.outline,
    titleText: String,
    itemOnClick: () -> Unit
) {
    CardM3(
        style = cardStyle,
        modifier = cardModifier,
        contentColor = cardContentColor,
        containerColor = cardContainerColor,
        border = cardBorder,
        onClick = itemOnClick,
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                icon?.let {
                    ImageViewM3(
                        painter = painterResource(it),
                        style = iconStyle,
                        size = iconSize,
                        autoIconSize = autoIconSize,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                endIcon?.let {
                    ImageViewM3(
                        painter = painterResource(it),
                        style = endIconStyle,
                        size = endIconSize,
                        autoIconSize = autoEndIconSize,
                        contentColor = endIconTint
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ScrollableTextBox(
                    text = titleText,
                    textStyle = titleTextStyle,
                    color = titleTextColor
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                subtitleText?.let { text ->
                    ScrollableTextBox(
                        text = text,
                        textStyle = subtitleTextStyle,
                        color = subtitleTextColor
                    )
                }
            }
        }
    }
}