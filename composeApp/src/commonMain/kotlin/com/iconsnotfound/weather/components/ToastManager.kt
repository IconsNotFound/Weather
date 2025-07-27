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

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.*

@Composable
fun SharedToastHost(): (String, Long) -> Unit {
    var message by remember { mutableStateOf<String?>(null) }
    var visible by remember { mutableStateOf(false) }
    var dismissJob by remember { mutableStateOf<Job?>(null) }

    if (visible && message != null) {
        Popup(
            alignment = Alignment.BottomCenter,
            offset = IntOffset(0, -80),
            properties = PopupProperties(focusable = false)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFF323232), RoundedCornerShape(8.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(message!!, color = Color.White)
            }
        }
    }

    return remember {
        { msg: String, duration: Long ->
            message = msg
            visible = true
            dismissJob?.cancel()
            dismissJob = CoroutineScope(Dispatchers.Main).launch {
                delay(duration)
                visible = false
                message = null
            }
        }
    }
}
