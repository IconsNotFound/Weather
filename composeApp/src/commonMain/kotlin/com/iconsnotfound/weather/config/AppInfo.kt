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

package com.iconsnotfound.weather.config

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.app_contact
import weather.composeapp.generated.resources.app_name
import weather.composeapp.generated.resources.app_version

object AppInfo {
    val APP_ID = "com_iconsnotfound_weather"

    @Composable
    fun userAgent(): String {
        val appName = stringResource(Res.string.app_name)
        val appVersion = stringResource(Res.string.app_version)
        val appContact = stringResource(Res.string.app_contact)
        return "$appName/$appVersion (+$appContact)"
    }
}
