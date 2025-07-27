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
import com.iconsnotfound.weather.config.AppInfo
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.Properties
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class JVMPlatform: Platform {
    //override val name: String = "Java ${System.getProperty("java.version")}"
    override val name: String = getOSVersionInfo()

    private fun getOSVersionInfo(): String {
        return when {
            System.getProperty("os.name").contains("Windows", ignoreCase = true) -> {
                val osName = System.getProperty("os.name")
                val osVersion = System.getProperty("os.version")
                "Windows $osName (Version $osVersion)"
            }

            System.getProperty("os.name").contains("Mac", ignoreCase = true) -> {
                val osName = System.getProperty("os.name")
                val osVersion = System.getProperty("os.version")
                val versionName = when {
                    osVersion.startsWith("13") -> "Ventura"
                    osVersion.startsWith("12") -> "Monterey"
                    osVersion.startsWith("11") -> "Big Sur"
                    osVersion.startsWith("10.16") -> "Big Sur"
                    osVersion.startsWith("10.15") -> "Catalina"
                    osVersion.startsWith("10.14") -> "Mojave"
                    else -> "Unknown version"
                }
                "macOS $osVersion ($versionName)"
            }

            else -> {
                try {
                    val osRelease = File("/etc/os-release").readLines()
                        .associate { line ->
                            line.split("=", limit = 2).let {
                                it[0] to it[1].removeSurrounding("\"")
                            }
                        }

                    val prettyName = osRelease["PRETTY_NAME"] ?: "Unknown Linux"
                    val versionId = osRelease["VERSION_ID"] ?: ""
                    "$prettyName $versionId"
                } catch (_: Exception) {
                    "Linux ${System.getProperty("os.name")} ${System.getProperty("os.version")}"
                }
            }
        }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {}

private var desktopToastHandler: ((String, Long) -> Unit)? = null

@Composable
fun DesktopToastHostComposable() {
    val handler = SharedToastHost()
    LaunchedEffect(Unit) {
        desktopToastHandler = handler
    }
}

actual fun showToast(message: String, duration: Long) {
    desktopToastHandler?.invoke(message, duration)
}

fun getAppConfigDir(appId: String): File {
    return when {
        System.getProperty("os.name").contains("Windows", ignoreCase = true) ->
            File(System.getenv("APPDATA") ?: System.getProperty("user.home"), appId)

        System.getProperty("os.name").contains("Mac", ignoreCase = true) ->
            File(System.getProperty("user.home"), "Library/Application Support/$appId")

        else -> {
            val xdgConfigHome = System.getenv("XDG_CONFIG_HOME")
            val configBase = if (xdgConfigHome != null) File(xdgConfigHome)
            else File(System.getProperty("user.home"), ".config")
            File(configBase, appId)
        }
    }.apply { mkdirs() }
}

actual fun createSettings(): Settings {
    val dir = getAppConfigDir(AppInfo.APP_ID)
    val file = File(dir, "settings.properties")
    val properties = Properties()
    val lock = ReentrantReadWriteLock()

    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    } else {
        file.createNewFile()
    }

    fun save() {
        lock.write {
            file.outputStream().use { properties.store(it, null) }
        }
    }

    return object : Settings {
        override val keys: Set<String>
            get() = lock.read { properties.stringPropertyNames() }

        override val size: Int
            get() = lock.read { properties.size }

        override fun clear() {
            lock.write {
                properties.clear()
                save()
            }
        }

        override fun remove(key: String) {
            lock.write {
                properties.remove(key)
                save()
            }
        }

        override fun hasKey(key: String): Boolean {
            return lock.read { properties.containsKey(key) }
        }

        override fun putString(key: String, value: String) {
            lock.write {
                properties.setProperty(key, value)
                save()
            }
        }

        override fun getString(key: String, defaultValue: String): String {
            return lock.read { properties.getProperty(key, defaultValue) }
        }

        override fun getStringOrNull(key: String): String? {
            return lock.read { properties.getProperty(key) }
        }

        override fun putInt(key: String, value: Int) = putString(key, value.toString())

        override fun getInt(key: String, defaultValue: Int): Int =
            getString(key, defaultValue.toString()).toIntOrNull() ?: defaultValue

        override fun getIntOrNull(key: String): Int? = getStringOrNull(key)?.toIntOrNull()

        override fun putLong(key: String, value: Long) = putString(key, value.toString())

        override fun getLong(key: String, defaultValue: Long): Long =
            getString(key, defaultValue.toString()).toLongOrNull() ?: defaultValue

        override fun getLongOrNull(key: String): Long? = getStringOrNull(key)?.toLongOrNull()

        override fun putFloat(key: String, value: Float) = putString(key, value.toString())

        override fun getFloat(key: String, defaultValue: Float): Float =
            getString(key, defaultValue.toString()).toFloatOrNull() ?: defaultValue

        override fun getFloatOrNull(key: String): Float? = getStringOrNull(key)?.toFloatOrNull()

        override fun putDouble(key: String, value: Double) = putString(key, value.toString())

        override fun getDouble(key: String, defaultValue: Double): Double =
            getString(key, defaultValue.toString()).toDoubleOrNull() ?: defaultValue

        override fun getDoubleOrNull(key: String): Double? = getStringOrNull(key)?.toDoubleOrNull()

        override fun putBoolean(key: String, value: Boolean) = putString(key, value.toString())

        override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
            when (getStringOrNull(key)) {
                "true" -> true
                "false" -> false
                else -> defaultValue
            }

        override fun getBooleanOrNull(key: String): Boolean? =
            when (getStringOrNull(key)) {
                "true" -> true
                "false" -> false
                else -> null
            }
    }
}

actual fun openUrl(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
    }
}

actual fun isSystemDefaultThemeDarkTheme(): Boolean {
    val osName = System.getProperty("os.name").lowercase()
    return when {
        osName.contains("mac") -> {
            val result = ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle")
                .start().inputStream.bufferedReader().readText().trim()
            result.equals("Dark", ignoreCase = true)
        }
        osName.contains("linux") -> {
            val result = ProcessBuilder("gsettings", "get", "org.gnome.desktop.interface", "gtk-theme")
                .start().inputStream.bufferedReader().readText().trim().lowercase()
            result.contains("dark")
        }
        osName.contains("windows") -> {
            val result = ProcessBuilder("reg", "query", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "/v", "AppsUseLightTheme")
                .start().inputStream.bufferedReader().readText()
            !result.contains("0x1")
        }
        else -> false
    }
}