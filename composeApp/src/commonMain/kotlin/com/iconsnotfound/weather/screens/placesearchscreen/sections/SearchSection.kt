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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.SettingsHolder.settings
import com.iconsnotfound.weather.attribution.PhotonAttribution
import com.iconsnotfound.weather.components.TextInput
import com.iconsnotfound.weather.config.AppInfo
import com.iconsnotfound.weather.data.AppDataStore
import com.iconsnotfound.weather.data.AppDataStore.getSearchRateLimit
import com.iconsnotfound.weather.data.AppDataStore.hasSearchAttemptCrossedRateLimit
import com.iconsnotfound.weather.data.AppDataStore.hasSearchCrossedRateLimit
import com.iconsnotfound.weather.data.AppDataStore.recordSearch
import com.iconsnotfound.weather.lib.TimeUtils
import com.iconsnotfound.weather.network.places.PlacesApi
import com.iconsnotfound.weather.network.places.PlacesModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.no_results_found
import weather.composeapp.generated.resources.search_a_place
import weather.composeapp.generated.resources.search_failed
import weather.composeapp.generated.resources.search_is_slower
import weather.composeapp.generated.resources.search_round
import weather.composeapp.generated.resources.server_network_unreachable

@Composable
fun SearchSection(
    query: MutableState<String>,
    searchJob: MutableState<Job?>,
    coroutineScope: CoroutineScope,
    isLoading: MutableState<Boolean>,
    results: MutableState<List<PlacesModels>>
) {
    val errorMessage = remember { mutableStateOf<String?>(null) }
    var bWaitForResult = remember { mutableStateOf(false) }
    var totalSearch = remember { mutableStateOf(AppDataStore.getSearchCountLastHour(settings)) }
    var totalSearchAttempt = remember { mutableStateOf(AppDataStore.getSearchAttemptCountLastHour(settings)) }
    var hasSearchCrossedLimits = remember { hasSearchCrossedRateLimit(settings) }
    var hasSearchAttemptCrossedLimits = remember { hasSearchAttemptCrossedRateLimit(settings) }
    var lastSearchTime by remember { mutableStateOf(0L) }

    val userAgentString = AppInfo.userAgent()

    TextInput(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        value = query.value,
        onValueChange = { newText ->
            query.value = newText

            searchJob.value?.cancel()
            isLoading.value = false

            searchJob.value = coroutineScope.launch {
                bWaitForResult.value = true
                delay(1000)
                val now = TimeUtils.getCurrentTimeInMillis()
                val elapsed = now - lastSearchTime

                if (query.value.isNotBlank() && elapsed >= 2000L) {
                    errorMessage.value = null
                    isLoading.value = true
                    AppDataStore.recordSearchAttempt(settings)
                    totalSearchAttempt.value = AppDataStore.getSearchAttemptCountLastHour(settings)
                    delay(getSearchRateLimit(settings))

                    try {
                        results.value = PlacesApi.search(query.value.trim(), userAgent = userAgentString)
                        recordSearch(settings)
                        totalSearch.value = AppDataStore.getSearchCountLastHour(settings)
                    } catch (e: Exception) {
                        e.message?.let {
                            if(e.cause != null) errorMessage.value = it
                            else errorMessage.value = null
                        }
                        results.value = emptyList()
                    }
                    isLoading.value = false
                    bWaitForResult.value = false
                } else {
                    results.value = emptyList()
                    bWaitForResult.value = false
                }
            } },
        label = stringResource(Res.string.search_a_place),
        painter = painterResource(Res.drawable.search_round)
    )

    if(hasSearchCrossedLimits || hasSearchAttemptCrossedLimits){
        Text(
            text = stringResource(Res.string.search_is_slower),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }

    PhotonAttribution()

    if (errorMessage.value != null) {
        Column(
            modifier = Modifier.padding(top = 64.dp).widthIn(max = 360.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.search_failed),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = stringResource(Res.string.server_network_unreachable),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

    } else if (results.value.isEmpty() && query.value.isNotBlank() && !bWaitForResult.value) {
        Column(
            modifier = Modifier.padding(top = 64.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.no_results_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        bWaitForResult.value = false
    }

    if(query.value.isEmpty()) {
        errorMessage.value = null
    }
}