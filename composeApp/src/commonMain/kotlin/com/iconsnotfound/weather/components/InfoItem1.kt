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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoItem1(
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
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    else null,
    headerRowModifier: Modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    headerRowHorizontalArrangement:  Arrangement.Horizontal = Arrangement.SpaceBetween,
    headerRowVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    headerRowTitle: String? = null,
    headerRowIcon: DrawableResource? = null,
    headerRowTitleTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    headerRowTitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    headerRowIconStyle: ImageStyle = ImageStyle.Icon,
    headerRowIconSize: Dp = 24.dp,
    headerRowIconAutoIconSize: Boolean = false,
    bodyRowModifier: Modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).fillMaxHeight(),
    bodyRowHorizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    bodyRowVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    bodyLeftColumnWeight: Float = 1f,
    bodyLeftColumnHorizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    bodyLeftColumnVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    bodyRightColumnWeight: Float = 1f,
    bodyRightColumnHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    bodyRightColumnVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
    bodyLeftTextUnit: String? = null,
    bodyLeftTextUnitWithVal: Boolean = true,
    bodyLeftTextStyle: TextStyle = MaterialTheme.typography.displaySmall,
    bodyLeftTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bodyLeftUnitStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    bodyLeftUnitColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    bodyLeftUnitModifier: Modifier = Modifier,

    bodyRightTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    bodyRightTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bodyLeftText: String,
    bodyRightText: String,

    ) {
    CardM3(
        style = cardStyle,
        modifier = cardModifier,
        containerColor = cardContainerColor,
        contentColor = cardContentColor,
        border = cardBorder,
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = headerRowModifier,
            horizontalArrangement = headerRowHorizontalArrangement,
            verticalAlignment = headerRowVerticalAlignment
        ) {
            headerRowTitle?.let {
                ScrollableTextBox(
                    text = it,
                    textStyle = headerRowTitleTextStyle,
                    color = headerRowTitleColor
                )
            }
            headerRowIcon?.let {
                ImageViewM3(
                    painter = painterResource(it),
                    style = headerRowIconStyle,
                    size = headerRowIconSize,
                    autoIconSize = headerRowIconAutoIconSize
                )
            }
        }

        Row(
            modifier = bodyRowModifier,
            horizontalArrangement = bodyRowHorizontalArrangement,
            verticalAlignment = bodyRowVerticalAlignment
        ) {
            Row (
                modifier = Modifier.weight(bodyLeftColumnWeight),
                horizontalArrangement =  bodyLeftColumnHorizontalArrangement,
                verticalAlignment = bodyLeftColumnVerticalAlignment
            ) {
                val title = if(bodyLeftTextUnit != null && bodyLeftTextUnitWithVal) {
                    "$bodyLeftText$bodyLeftTextUnit"
                } else bodyLeftText
                Text(
                    text = title,
                    style = bodyLeftTextStyle,
                    color = bodyLeftTextColor
                )
                if(bodyLeftTextUnit != null && !bodyLeftTextUnitWithVal) {
                    Text(
                        text = bodyLeftTextUnit,
                        style = bodyLeftUnitStyle,
                        color = bodyLeftUnitColor,
                        modifier = bodyLeftUnitModifier
                    )
                }
            }

            Column (
                modifier = Modifier.weight(bodyRightColumnWeight),
                horizontalAlignment =  bodyRightColumnHorizontalAlignment,
                verticalArrangement = bodyRightColumnVerticalArrangement
            ) {
                Text(
                    text = bodyRightText,
                    style = bodyRightTextStyle,
                    color = bodyRightTextColor
                )
            }
        }
    }
}