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

package com.iconsnotfound.weather.screens.placesearchscreen.sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.InfoItem3
import com.iconsnotfound.weather.network.places.PlacesModels
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.data.Places
import com.iconsnotfound.weather.showToast
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.check_circle_round
import weather.composeapp.generated.resources.default_place_updated
import weather.composeapp.generated.resources.place_added
import weather.composeapp.generated.resources.place_already_saved
import weather.composeapp.generated.resources.saved_places_limit_reached

@Composable
fun ResultSection(
    results: MutableState<List<PlacesModels>>,
    onBack: (Boolean)-> Unit,
    settings: Settings,
    fromHomeScreen: Boolean
) {
    val alreadyExists = stringResource(Res.string.place_already_saved)
    val msgHasSavedPlacesLimitReached = stringResource(Res.string.saved_places_limit_reached)
    val msgSuccess = stringResource(Res.string.place_added)
    val defaultPlaceUpdated = stringResource(Res.string.default_place_updated)
    val (savedPlacesLimitReached, maxLimit) = AppDataStore.hasSavedPlacesLimitReached(settings)
    Spacer(modifier = Modifier.height(32.dp))
    results.value.forEach {
        val id = "${it.display_name}_${it.lat}_${it.lon}".hashCode().toString()
        val location = Places(
            id = id,
            name = it.display_name,
            lat = it.lat,
            lon = it.lon,
            osm_value = it.osm_value,
            city = it.city,
            country = it.country,
        )
        InfoItem3(
            cardStyle = CardStyle.Outlined,
            location = location,
            infoItemOnClick = {
                if(!AppDataStore.exists(id, settings)) {
                    if(savedPlacesLimitReached) {
                        showToast("$msgHasSavedPlacesLimitReached $maxLimit")
                        return@InfoItem3
                    }
                    AppDataStore.add(location, settings, msgSuccess)
                    if(fromHomeScreen || AppDataStore.getTotalSavedPlaces(settings) == 1) {
                        AppDataStore.setDefaultPlace(id, settings, msgSuccess = defaultPlaceUpdated)
                    }
                    onBack(fromHomeScreen)
                }
                else showToast(alreadyExists)
            },
            isDefault = AppDataStore.exists(id, settings),
            isDefaultIcon = painterResource(Res.drawable.check_circle_round),
        )
    }
}