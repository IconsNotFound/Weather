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
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CardStyle {
    Filled, Elevated, Outlined
}

@Composable
fun CardM3(
    modifier: Modifier = Modifier,
    style: CardStyle = CardStyle.Filled,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = when (style) {
        CardStyle.Filled -> MaterialTheme.colorScheme.secondaryContainer
        CardStyle.Elevated-> MaterialTheme.colorScheme.surface
        CardStyle.Outlined -> Color.Transparent
    },
    contentColor: Color = when (style) {
        CardStyle.Filled -> MaterialTheme.colorScheme.onPrimaryContainer
        CardStyle.Elevated -> MaterialTheme.colorScheme.onSurface
        CardStyle.Outlined -> MaterialTheme.colorScheme.onSurface
    },
    border: BorderStroke? = if (style == CardStyle.Outlined)
        BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outline)
    else null,
    elevation: Dp = when (style) {
        CardStyle.Filled -> 0.dp
        CardStyle.Elevated -> 4.dp
        CardStyle.Outlined -> 0.dp
    },
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable(
                onClick = onClick,
                indication = indication,
                interactionSource = interactionSource
            )
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor),
        border = border,
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
