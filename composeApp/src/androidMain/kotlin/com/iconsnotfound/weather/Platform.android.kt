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

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import com.iconsnotfound.weather.config.AppInfo
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import androidx.activity.compose.BackHandler as AndroidBackHandler

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    AndroidBackHandler(enabled = enabled, onBack = onBack)
}

private var toastContextProvider: (() -> Context?)? = null

fun provideToastContext(provider: () -> Context?) {
    toastContextProvider = provider
}

actual fun showToast(message: String, duration: Long) {
    val context = AndroidContextHolder.applicationContext
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

actual fun createSettings(): Settings {
    val context = AndroidContextHolder.applicationContext
    val prefs = context.getSharedPreferences(
        AppInfo.APP_ID,
        Context.MODE_PRIVATE
    )
    return SharedPreferencesSettings(prefs)
}

actual fun openUrl(url: String) {
    val context = AndroidContextHolder.applicationContext
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

actual fun isSystemDefaultThemeDarkTheme(): Boolean {
    val uiMode = Resources.getSystem().configuration.uiMode
    return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }
}