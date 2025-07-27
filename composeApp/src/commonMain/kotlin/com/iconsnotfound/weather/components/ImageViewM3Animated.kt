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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ImageViewM3Animated(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String? = null,
    style: ImageStyle = ImageStyle.Icon,
    onClick: (() -> Unit)? = null,
    size: Dp = 40.dp,
    iconSize: Dp = 24.dp,
    autoIconSize: Boolean = true,
    shape: Shape = CircleShape,
    isRotating: Boolean = true,
    rotationDurationMs: Int = 2000,
    backgroundColor: Color = when (style) {
        ImageStyle.Filled -> MaterialTheme.colorScheme.primary
        ImageStyle.Tonal -> MaterialTheme.colorScheme.secondaryContainer
        else -> Color.Transparent
    },
    contentColor: Color = when (style) {
        ImageStyle.Filled -> MaterialTheme.colorScheme.onPrimary
        ImageStyle.Tonal -> MaterialTheme.colorScheme.onSecondaryContainer
        ImageStyle.Icon -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    },
    border: BorderStroke? = if (style == ImageStyle.Outlined)
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    else null,
    elevation: Dp = if (style == ImageStyle.Filled) 2.dp else 0.dp,
    initialRotationVal: Float = 0f,
    targetRotationVal: Float = 360f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = initialRotationVal,
        targetValue = targetRotationVal,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = rotationDurationMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    val imageContent: @Composable () -> Unit = {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(if (autoIconSize) size * 0.6f else iconSize)
            )
        } else if (painter != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(if (autoIconSize) size * 0.6f else size),
                colorFilter = ColorFilter.tint(contentColor)
            )
        }
    }

    Surface(
        modifier = modifier
            .size(size)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .rotate(if (isRotating) angle else 0f),
        shape = shape,
        color = backgroundColor,
        tonalElevation = elevation,
        border = border,
    ) {
        Box(contentAlignment = Alignment.Center) {
            imageContent()
        }
    }
}