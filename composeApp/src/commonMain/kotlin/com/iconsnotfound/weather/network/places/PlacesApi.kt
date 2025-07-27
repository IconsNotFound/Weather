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

package com.iconsnotfound.weather.network.places

import com.iconsnotfound.weather.SettingsHolder.settings
import com.iconsnotfound.weather.config.ApiConfig
import com.iconsnotfound.weather.createHttpClient
import com.iconsnotfound.weather.data.ApiCache
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object PlacesApi {
    private val client = createHttpClient()
    val apiCache = ApiCache(settings)

    suspend fun search(query: String, userAgent: String): List<PlacesModels> {
        if (query.isBlank()) return emptyList()
        apiCache.get(query.trim())?.let { return it }

        //println("UserAgent(Search): $userAgent")
        val url = ApiConfig.photonBaseUrl
        val response = client.get(url) {
            parameter("q", query)
            parameter("limit", 5)
            accept(ContentType.Application.Json)
            header("User-Agent", userAgent)
        }

        val json = Json { ignoreUnknownKeys = true }
        val text = response.bodyAsText()

        val parsed = json.parseToJsonElement(text).jsonObject
        val features = parsed["features"]?.jsonArray ?: return emptyList()

        val results = features.mapNotNull { feature ->
            try {
                val props = feature.jsonObject["properties"]!!.jsonObject
                val geometry = feature.jsonObject["geometry"]!!.jsonObject
                val coords = geometry["coordinates"]!!.jsonArray
                val lon = coords[0].toString().toDoubleOrNull()
                val lat = coords[1].toString().toDoubleOrNull()

                if (lat != null && lon != null) {
                    PlacesModels(
                        place_id = props["osm_id"]?.jsonPrimitive?.contentOrNull
                            ?: "(Unknown)",
                        display_name = props["name"]?.jsonPrimitive?.contentOrNull ?: "(unknown)",
                        lat = lat.toString(),
                        lon = lon.toString(),
                        osm_value = props["osm_value"]?.jsonPrimitive?.contentOrNull
                            ?: "",
                        city = props["city"]?.jsonPrimitive?.contentOrNull
                            ?: "",
                        country = props["country"]?.jsonPrimitive?.contentOrNull
                            ?: ""
                    )
                } else null
            } catch (_: Exception) {
                null
            }
        }

        apiCache.put(query.trim(), results)
        return results
    }
}
