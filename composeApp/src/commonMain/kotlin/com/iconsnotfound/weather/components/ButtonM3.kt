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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class ButtonStyle {
    Filled, Tonal, Outlined, Text
}

@Composable
fun ButtonM3(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    text: String? = null,
    style: ButtonStyle = ButtonStyle.Filled,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color = when (style) {
        ButtonStyle.Filled -> MaterialTheme.colorScheme.primary
        ButtonStyle.Tonal -> MaterialTheme.colorScheme.secondaryContainer
        else -> Color.Transparent
    },
    contentColor: Color = when (style) {
        ButtonStyle.Filled -> MaterialTheme.colorScheme.onPrimary
        ButtonStyle.Tonal -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    },
    iconTint: Color = contentColor,
    elevation: ButtonElevation? = if (style == ButtonStyle.Filled) {
        ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp)
    } else null,
    border: BorderStroke? = if (style == ButtonStyle.Outlined)
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    else null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
) {
    val content: @Composable RowScope.() -> Unit = {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            if (text != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        if (text != null) {
            Text(text = text, color = contentColor)
        }
    }

    when (style) {
        ButtonStyle.Filled -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            elevation = elevation,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = contentPadding,
            content = content
        )

        ButtonStyle.Tonal -> FilledTonalButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = contentPadding,
            content = content
        )

        ButtonStyle.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            border = border,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor
            ),
            contentPadding = contentPadding,
            content = content
        )

        ButtonStyle.Text -> TextButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = contentColor
            ),
            contentPadding = contentPadding,
            content = content
        )
    }
}
