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

package com.iconsnotfound.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.iconsnotfound.weather.components.SharedToastHost
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.localStorage
import kotlinx.browser.window

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {}

private var toastWebHandler: ((String, Long) -> Unit)? = null

@Composable
fun WebToastHostComposable() {
    val handler = SharedToastHost()
    LaunchedEffect(Unit) {
        toastWebHandler = handler
    }
}

actual fun showToast(message: String, duration: Long) {
    toastWebHandler?.invoke(message, duration)
}

actual fun createSettings(): Settings {
    return object : Settings {
        override val keys: Set<String>
            get() {
                val keys = mutableSetOf<String>()
                for (i in 0 until localStorage.length) {
                    localStorage.key(i)?.let { keys.add(it) }
                }
                return keys
            }

        override val size: Int
            get() = localStorage.length

        override fun clear() {
            localStorage.clear()
        }

        override fun remove(key: String) {
            localStorage.removeItem(key)
        }

        override fun hasKey(key: String): Boolean {
            return localStorage.getItem(key) != null
        }

        override fun putInt(key: String, value: Int) {
            localStorage.setItem(key, value.toString())
        }

        override fun getInt(key: String, defaultValue: Int): Int {
            return localStorage.getItem(key)?.toIntOrNull() ?: defaultValue
        }

        override fun getIntOrNull(key: String): Int? {
            return localStorage.getItem(key)?.toIntOrNull()
        }

        override fun putLong(key: String, value: Long) {
            localStorage.setItem(key, value.toString())
        }

        override fun getLong(key: String, defaultValue: Long): Long {
            return localStorage.getItem(key)?.toLongOrNull() ?: defaultValue
        }

        override fun getLongOrNull(key: String): Long? {
            return localStorage.getItem(key)?.toLongOrNull()
        }

        override fun putString(key: String, value: String) {
            localStorage.setItem(key, value)
        }

        override fun getString(key: String, defaultValue: String): String {
            return localStorage.getItem(key) ?: defaultValue
        }

        override fun getStringOrNull(key: String): String? {
            return localStorage.getItem(key)
        }

        override fun putFloat(key: String, value: Float) {
            localStorage.setItem(key, value.toString())
        }

        override fun getFloat(key: String, defaultValue: Float): Float {
            return localStorage.getItem(key)?.toFloatOrNull() ?: defaultValue
        }

        override fun getFloatOrNull(key: String): Float? {
            return localStorage.getItem(key)?.toFloatOrNull()
        }

        override fun putDouble(key: String, value: Double) {
            localStorage.setItem(key, value.toString())
        }

        override fun getDouble(key: String, defaultValue: Double): Double {
            return localStorage.getItem(key)?.toDoubleOrNull() ?: defaultValue
        }

        override fun getDoubleOrNull(key: String): Double? {
            return localStorage.getItem(key)?.toDoubleOrNull()
        }

        override fun putBoolean(key: String, value: Boolean) {
            localStorage.setItem(key, value.toString())
        }

        override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
            return when (localStorage.getItem(key)) {
                "true" -> true
                "false" -> false
                else -> defaultValue
            }
        }

        override fun getBooleanOrNull(key: String): Boolean? {
            return when (localStorage.getItem(key)) {
                "true" -> true
                "false" -> false
                else -> null
            }
        }

    }
}

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}

actual fun isSystemDefaultThemeDarkTheme(): Boolean {
    return window.matchMedia("(prefers-color-scheme: dark)").matches
}

actual fun createHttpClient(): HttpClient {
    return HttpClient(Js) {
        install(ContentNegotiation) {
            json()
        }
    }
}