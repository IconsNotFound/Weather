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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.end_icon
import weather.composeapp.generated.resources.start_icon

@Composable
fun HorizontalProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    progressBarHeight: Dp = 12.dp,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    startIcon: Painter? = null,
    endIcon: Painter? = null,
    startText: String? = null,
    endText: String? = null,
    progressTipIcon: Painter? = null,
    iconSize: Dp = 24.dp,
    iconTextSpacing: Dp = 4.dp,
    tipIconSize: Dp = 32.dp,
    tipIconOffsetY: Dp = (-20).dp,
    animationDurationMs: Int = 1200,
    progressTipStyle: ImageStyle = ImageStyle.Filled,
    startLabel: String? = null,
    endLabel: String? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDurationMs),
        label = "progress animation"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                startIcon?.let {
                    Image(
                        painter = it,
                        contentDescription = stringResource(Res.string.start_icon),
                        modifier = Modifier.size(iconSize),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                startLabel?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                endIcon?.let {
                    Image(
                        painter = it,
                        contentDescription = stringResource(Res.string.end_icon),
                        modifier = Modifier.size(iconSize),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                endLabel?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val maxWidthPx = constraints.maxWidth.toFloat()
            val density = LocalDensity.current
            val animatedX = animatedProgress * maxWidthPx

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressBarHeight)
                    .background(backgroundColor, RoundedCornerShape(50))
            )

            Box(
                modifier = Modifier
                    .width(with(density) { animatedX.toDp() })
                    .height(progressBarHeight)
                    .background(progressColor, RoundedCornerShape(50))
            )

            if (progressTipIcon != null) {
                ImageViewM3(
                    modifier = Modifier
                        .size(tipIconSize)
                        .offset {
                            IntOffset(
                                x = (animatedX - with(density) { tipIconSize.toPx() } / 2)
                                    .toInt()
                                    .coerceIn(0, maxWidthPx.toInt() - tipIconSize.roundToPx()),
                                y = ((progressBarHeight - tipIconSize) / 2).roundToPx()
                            )
                        },
                    painter = progressTipIcon,
                    style = progressTipStyle,
                    size = 28.dp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                startText?.let {
                    Spacer(Modifier.height(iconTextSpacing))
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                endText?.let {
                    Spacer(Modifier.height(iconTextSpacing))
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}