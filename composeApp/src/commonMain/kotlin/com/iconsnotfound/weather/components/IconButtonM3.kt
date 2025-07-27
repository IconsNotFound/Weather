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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class IconButtonStyle {
    Filled, Outlined, Tonal, Text, Image
}

@Composable
fun IconButtonM3(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String? = null,
    iconSize: Dp = 24.dp,
    buttonSize: Dp = 48.dp,
    style: IconButtonStyle = IconButtonStyle.Filled,
    containerColor: Color = when (style) {
        IconButtonStyle.Filled -> MaterialTheme.colorScheme.primary
        IconButtonStyle.Tonal -> MaterialTheme.colorScheme.secondaryContainer
        else -> Color.Transparent
    },
    contentColor: Color = when (style) {
        IconButtonStyle.Filled -> MaterialTheme.colorScheme.onPrimary
        IconButtonStyle.Tonal -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    },
    iconTint: Color = when (style) {
        IconButtonStyle.Filled -> MaterialTheme.colorScheme.onPrimary
        IconButtonStyle.Tonal -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    },
    border: BorderStroke? = if (style == IconButtonStyle.Outlined)
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    else null
) {
    val commonModifier = modifier.size(buttonSize)

    val content: @Composable RowScope.() -> Unit = {
        if(icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        }
        else if (painter != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(iconSize),
                colorFilter = ColorFilter.tint(contentColor)
            )
        }
    }

    when (style) {
        IconButtonStyle.Filled -> Button(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
            content = content
        )

        IconButtonStyle.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            border = border,
            content = content
        )

        IconButtonStyle.Tonal -> FilledTonalButton(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            shape = CircleShape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            content = content
        )

        IconButtonStyle.Text -> TextButton(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            content = content
        )

        IconButtonStyle.Image -> TextButton(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            content = {
                CompositionLocalProvider(LocalContentColor provides Color.Unspecified) {
                    painter?.let {
                        ImageViewM3(
                            painter = it,
                            autoIconSize = true,
                            contentColor = null
                        )
                    }
                }
            }
        )
    }
}
