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

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.clear_sky_day_rounded
import weather.composeapp.generated.resources.clear_sky_night_rounded
import weather.composeapp.generated.resources.cloud_interpret_clear
import weather.composeapp.generated.resources.cloud_interpret_mostly_clear
import weather.composeapp.generated.resources.cloud_interpret_mostly_cloudy
import weather.composeapp.generated.resources.cloud_interpret_overcast
import weather.composeapp.generated.resources.cloud_interpret_partly_cloudy
import weather.composeapp.generated.resources.cloud_interpret_unknown
import weather.composeapp.generated.resources.cloud_rounded
import weather.composeapp.generated.resources.dew_point_desc_comfortable
import weather.composeapp.generated.resources.dew_point_desc_dry
import weather.composeapp.generated.resources.dew_point_desc_humid
import weather.composeapp.generated.resources.dew_point_desc_slightly_humid
import weather.composeapp.generated.resources.dew_point_desc_very_humid
import weather.composeapp.generated.resources.direction_east
import weather.composeapp.generated.resources.direction_east_northeast
import weather.composeapp.generated.resources.direction_east_southeast
import weather.composeapp.generated.resources.direction_north
import weather.composeapp.generated.resources.direction_north_northeast
import weather.composeapp.generated.resources.direction_north_northwest
import weather.composeapp.generated.resources.direction_northeast
import weather.composeapp.generated.resources.direction_northwest
import weather.composeapp.generated.resources.direction_south
import weather.composeapp.generated.resources.direction_south_southeast
import weather.composeapp.generated.resources.direction_south_southwest
import weather.composeapp.generated.resources.direction_southeast
import weather.composeapp.generated.resources.direction_southwest
import weather.composeapp.generated.resources.direction_west
import weather.composeapp.generated.resources.direction_west_northwest
import weather.composeapp.generated.resources.direction_west_southwest
import weather.composeapp.generated.resources.drizzle_rounded
import weather.composeapp.generated.resources.fog_rounded
import weather.composeapp.generated.resources.heavy_freezing_rain_rounded
import weather.composeapp.generated.resources.heavy_snowfall_rounded
import weather.composeapp.generated.resources.light_freezing_rain_rounded
import weather.composeapp.generated.resources.light_snowfall_rounded
import weather.composeapp.generated.resources.mainly_clear_day_rounded
import weather.composeapp.generated.resources.mainly_clear_night_rounded
import weather.composeapp.generated.resources.overcast_rounded
import weather.composeapp.generated.resources.partly_cloudy_day_rounded
import weather.composeapp.generated.resources.partly_cloudy_night_rounded
import weather.composeapp.generated.resources.precipitation_desc_extreme_rain
import weather.composeapp.generated.resources.precipitation_desc_heavy_rain
import weather.composeapp.generated.resources.precipitation_desc_light_rain
import weather.composeapp.generated.resources.precipitation_desc_moderate_rain
import weather.composeapp.generated.resources.precipitation_desc_no_rain
import weather.composeapp.generated.resources.precipitation_desc_very_heavy_rain
import weather.composeapp.generated.resources.precipitation_desc_very_light_rain
import weather.composeapp.generated.resources.rain_rounded
import weather.composeapp.generated.resources.thunderstorm_rounded
import weather.composeapp.generated.resources.uv_desc_extreme
import weather.composeapp.generated.resources.uv_desc_high
import weather.composeapp.generated.resources.uv_desc_low
import weather.composeapp.generated.resources.uv_desc_moderate
import weather.composeapp.generated.resources.uv_desc_very_high
import weather.composeapp.generated.resources.visibility_desc_excellent
import weather.composeapp.generated.resources.visibility_desc_good
import weather.composeapp.generated.resources.visibility_desc_moderate
import weather.composeapp.generated.resources.visibility_desc_poor
import weather.composeapp.generated.resources.visibility_desc_very_poor
import weather.composeapp.generated.resources.weather_desc_clear_sky
import weather.composeapp.generated.resources.weather_desc_dense_drizzle
import weather.composeapp.generated.resources.weather_desc_dense_freezing_drizzle
import weather.composeapp.generated.resources.weather_desc_depositing_rime_fog
import weather.composeapp.generated.resources.weather_desc_fog
import weather.composeapp.generated.resources.weather_desc_heavy_freezing_rain
import weather.composeapp.generated.resources.weather_desc_heavy_rain
import weather.composeapp.generated.resources.weather_desc_heavy_snow_fall
import weather.composeapp.generated.resources.weather_desc_heavy_snow_showers
import weather.composeapp.generated.resources.weather_desc_light_drizzle
import weather.composeapp.generated.resources.weather_desc_light_freezing_drizzle
import weather.composeapp.generated.resources.weather_desc_light_freezing_rain
import weather.composeapp.generated.resources.weather_desc_mainly_clear
import weather.composeapp.generated.resources.weather_desc_moderate_drizzle
import weather.composeapp.generated.resources.weather_desc_moderate_rain
import weather.composeapp.generated.resources.weather_desc_moderate_rain_showers
import weather.composeapp.generated.resources.weather_desc_moderate_snow_fall
import weather.composeapp.generated.resources.weather_desc_overcast
import weather.composeapp.generated.resources.weather_desc_partly_cloudy
import weather.composeapp.generated.resources.weather_desc_slight_rain
import weather.composeapp.generated.resources.weather_desc_slight_rain_showers
import weather.composeapp.generated.resources.weather_desc_slight_snow_fall
import weather.composeapp.generated.resources.weather_desc_slight_snow_showers
import weather.composeapp.generated.resources.weather_desc_snow_grains
import weather.composeapp.generated.resources.weather_desc_thunderstorm_heavy_hail
import weather.composeapp.generated.resources.weather_desc_thunderstorm_light_hail
import weather.composeapp.generated.resources.weather_desc_thunderstorm_no_hail
import weather.composeapp.generated.resources.weather_desc_unknown
import weather.composeapp.generated.resources.weather_desc_violent_rain_showers
import kotlin.math.roundToInt

object WeatherUtils {
    @Composable
    fun getWeatherDescription(code: Int): String = when (code) {
        0 -> stringResource(Res.string.weather_desc_clear_sky)
        1 -> stringResource(Res.string.weather_desc_mainly_clear)
        2 -> stringResource(Res.string.weather_desc_partly_cloudy)
        3 -> stringResource(Res.string.weather_desc_overcast)
        45 -> stringResource(Res.string.weather_desc_fog)
        48 -> stringResource(Res.string.weather_desc_depositing_rime_fog)
        51 -> stringResource(Res.string.weather_desc_light_drizzle)
        53 -> stringResource(Res.string.weather_desc_moderate_drizzle)
        55 -> stringResource(Res.string.weather_desc_dense_drizzle)
        56 -> stringResource(Res.string.weather_desc_light_freezing_drizzle)
        57 -> stringResource(Res.string.weather_desc_dense_freezing_drizzle)
        61 -> stringResource(Res.string.weather_desc_slight_rain)
        63 -> stringResource(Res.string.weather_desc_moderate_rain)
        65 -> stringResource(Res.string.weather_desc_heavy_rain)
        66 -> stringResource(Res.string.weather_desc_light_freezing_rain)
        67 -> stringResource(Res.string.weather_desc_heavy_freezing_rain)
        71 -> stringResource(Res.string.weather_desc_slight_snow_fall)
        73 -> stringResource(Res.string.weather_desc_moderate_snow_fall)
        75 -> stringResource(Res.string.weather_desc_heavy_snow_fall)
        77 -> stringResource(Res.string.weather_desc_snow_grains)
        80 -> stringResource(Res.string.weather_desc_slight_rain_showers)
        81 -> stringResource(Res.string.weather_desc_moderate_rain_showers)
        82 -> stringResource(Res.string.weather_desc_violent_rain_showers)
        85 -> stringResource(Res.string.weather_desc_slight_snow_showers)
        86 -> stringResource(Res.string.weather_desc_heavy_snow_showers)
        95 -> stringResource(Res.string.weather_desc_thunderstorm_no_hail)
        96 -> stringResource(Res.string.weather_desc_thunderstorm_light_hail)
        99 -> stringResource(Res.string.weather_desc_thunderstorm_heavy_hail)
        else -> stringResource(Res.string.weather_desc_unknown)
    }

    fun getWeatherIcon(
        code: Int,
        sunriseIso: String? = null,
        sunsetIso: String? = null
    ): DrawableResource = when (code) {
        0 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.clear_sky_night_rounded else Res.drawable.clear_sky_day_rounded
        1 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.mainly_clear_night_rounded else Res.drawable.mainly_clear_day_rounded
        2 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.partly_cloudy_night_rounded else Res.drawable.partly_cloudy_day_rounded
        3 -> (Res.drawable.overcast_rounded)
        in 45..48 -> (Res.drawable.fog_rounded)
        in 51..57 -> (Res.drawable.drizzle_rounded)
        in 61..65 -> (Res.drawable.rain_rounded)
        66 -> Res.drawable.light_freezing_rain_rounded
        67 -> Res.drawable.heavy_freezing_rain_rounded
        in 71..73, 85 -> (Res.drawable.light_snowfall_rounded)
        in 75..77, 86 -> (Res.drawable.heavy_snowfall_rounded)
        in 80..82 -> (Res.drawable.rain_rounded)
        in 95..99 -> (Res.drawable.thunderstorm_rounded)
        else -> (Res.drawable.cloud_rounded)
    }

    @Composable
    fun interpretCloudCover(cloudCover: Int): String = when (cloudCover) {
        in 0..10 -> stringResource(Res.string.cloud_interpret_clear)
        in 11..30 -> stringResource(Res.string.cloud_interpret_mostly_clear)
        in 31..50 -> stringResource(Res.string.cloud_interpret_partly_cloudy)
        in 51..70 -> stringResource(Res.string.cloud_interpret_mostly_cloudy)
        in 71..100 -> stringResource(Res.string.cloud_interpret_overcast)
        else -> stringResource(Res.string.cloud_interpret_unknown)
    }

    fun interpretCloudCoverIcon(
        code: Int,
        sunriseIso: String? = null,
        sunsetIso: String? = null
    ): DrawableResource = when (code) {
        in 0..10 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.clear_sky_night_rounded else Res.drawable.clear_sky_day_rounded
        in 11..30 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.mainly_clear_night_rounded else Res.drawable.mainly_clear_day_rounded
        in 31..50 -> if(TimeUtils.isNightNow(sunriseIso, sunsetIso)) Res.drawable.partly_cloudy_night_rounded else Res.drawable.partly_cloudy_day_rounded
        in 51..70 -> Res.drawable.cloud_rounded
        in 71..100 -> Res.drawable.overcast_rounded
        else -> Res.drawable.cloud_rounded
    }

    @Composable
    fun dewPointDescription(dpC: Double): String {
        return when {
            dpC < 10 -> stringResource(Res.string.dew_point_desc_dry)
            dpC < 16 -> stringResource(Res.string.dew_point_desc_comfortable)
            dpC < 21 -> stringResource(Res.string.dew_point_desc_slightly_humid)
            dpC < 25 -> stringResource(Res.string.dew_point_desc_humid)
            else -> stringResource(Res.string.dew_point_desc_very_humid)
        }
    }

    @Composable
    fun degreesToDirection(degrees: Int): String {
        val directions = listOf(
            stringResource(Res.string.direction_north),
            stringResource(Res.string.direction_north_northeast),
            stringResource(Res.string.direction_northeast),
            stringResource(Res.string.direction_east_northeast),

            stringResource(Res.string.direction_east),
            stringResource(Res.string.direction_east_southeast),
            stringResource(Res.string.direction_southeast),
            stringResource(Res.string.direction_south_southeast),

            stringResource(Res.string.direction_south),
            stringResource(Res.string.direction_south_southwest),
            stringResource(Res.string.direction_southwest),
            stringResource(Res.string.direction_west_southwest),

            stringResource(Res.string.direction_west),
            stringResource(Res.string.direction_west_northwest),
            stringResource(Res.string.direction_northwest),
            stringResource(Res.string.direction_north_northwest),
        )
        val index = ((degrees % 360) / 22.5).roundToInt() % 16
        return directions[index]
    }

    @Composable
    fun describePrecipitation(precipitationMm: Double): String {
        return when {
            precipitationMm == 0.0 -> stringResource(Res.string.precipitation_desc_no_rain)
            precipitationMm <= 1.0 -> stringResource(Res.string.precipitation_desc_very_light_rain)
            precipitationMm <= 2.5 -> stringResource(Res.string.precipitation_desc_light_rain)
            precipitationMm <= 7.6 -> stringResource(Res.string.precipitation_desc_moderate_rain)
            precipitationMm <= 15.0 -> stringResource(Res.string.precipitation_desc_heavy_rain)
            precipitationMm <= 50.0 -> stringResource(Res.string.precipitation_desc_very_heavy_rain)
            else -> stringResource(Res.string.precipitation_desc_extreme_rain)
        }
    }

    @Composable
    fun getUVRiskLevel(index: Float): String {
        return when {
            index < 3 -> stringResource(Res.string.uv_desc_low)
            index < 6 -> stringResource(Res.string.uv_desc_moderate)
            index < 8 -> stringResource(Res.string.uv_desc_high)
            index < 11 -> stringResource(Res.string.uv_desc_very_high)
            else -> stringResource(Res.string.uv_desc_extreme)
        }
    }

    @Composable
    fun getVisibilityDescription(km: Float): String {
        return when {
            km >= 10 -> stringResource(Res.string.visibility_desc_excellent)
            km >= 6 -> stringResource(Res.string.visibility_desc_good)
            km >= 2 -> stringResource(Res.string.visibility_desc_moderate)
            km >= 0.5 -> stringResource(Res.string.visibility_desc_poor)
            else -> stringResource(Res.string.visibility_desc_very_poor)
        }
    }

}