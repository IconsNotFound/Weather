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

package com.iconsnotfound.weather.lib

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import kotlin.math.round
import kotlin.math.roundToInt


fun parseBoldTags(input: String): AnnotatedString {
    val regex = Regex("<b>(.*?)</b>")
    return buildAnnotatedString {
        var lastIndex = 0
        for (match in regex.findAll(input)) {
            val range = match.range
            if (range.first > lastIndex) {
                append(input.substring(lastIndex, range.first))
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(match.groupValues[1])
            }
            lastIndex = range.last + 1
        }
        if (lastIndex < input.length) {
            append(input.substring(lastIndex))
        }
    }
}

fun parseMarkdownLinks(
    input: String,
    tag: String = "url",
    style: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Medium,
        color = Color(0xFF007169),
        textDecoration = TextDecoration.Underline
    )
): AnnotatedString {
    val regex = Regex("\\[(.+?)\\]\\((.+?)\\)")
    return buildAnnotatedString {
        var lastIndex = 0
        for (match in regex.findAll(input)) {
            val start = match.range.first
            if (lastIndex < start) {
                append(input.substring(lastIndex, start))
            }

            val displayText = match.groupValues[1]
            val url = match.groupValues[2]

            pushStringAnnotation(tag = tag, annotation = url)
            withStyle(
                style
            ) {
                append(displayText)
            }
            pop()

            lastIndex = match.range.last + 1
        }

        if (lastIndex < input.length) {
            append(input.substring(lastIndex))
        }
    }
}

fun String.padWithLeading(pad: String, length: Int = 2): String = this.let { str ->
    if (str.length < length) {
        "$pad$str"
    } else {
        str
    }
}

fun formatPrecipitation(value: Double): String {
    val roundedValue = if (value < 10.0) {
        (round(value * 10) / 10.0).let {
            if (it >= 10.0) it.toInt().toString() else it.toString()
        }
    } else {
        value.roundToInt().toString()
    }

    return if (value < 10.0 && roundedValue.endsWith(".0")) {
        roundedValue.substringBefore(".")
    } else {
        roundedValue
    }
}