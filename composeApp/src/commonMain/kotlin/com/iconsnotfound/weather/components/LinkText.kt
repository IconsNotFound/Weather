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

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.iconsnotfound.weather.lib.parseMarkdownLinks
import com.iconsnotfound.weather.openUrl

@Composable
fun LinkText(
    string: String,
    linkFontWeight: FontWeight = FontWeight.Medium,
    linkColor: Color = Color(0xFF007169),
    textDecoration: TextDecoration = TextDecoration.Underline,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    val annotated = remember(string) {
        parseMarkdownLinks(
            string,
            style = SpanStyle(
                fontWeight = linkFontWeight,
                color = linkColor,
                textDecoration = textDecoration
            )
        )
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotated,
        onTextLayout = { layoutResult = it },
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { position ->
                layoutResult?.let { layout ->
                    val offset = layout.getOffsetForPosition(position)

                    val line = layout.getLineForOffset(offset)
                    val top = layout.getLineTop(line)
                    val bottom = layout.getLineBottom(line)

                    if (position.y in top..bottom) {
                        annotated.getStringAnnotations(tag = "url", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                openUrl(annotation.item)
                            }
                    }
                }
            }
        },
        style = textStyle,
        color = textColor
    )
}