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

package com.iconsnotfound.weather.screens.homescreen.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iconsnotfound.weather.components.CardM3
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.ImageStyle
import com.iconsnotfound.weather.components.ImageViewM3
import com.iconsnotfound.weather.components.ImageViewM3Animated
import com.iconsnotfound.weather.components.ScrollableTextBox
import com.iconsnotfound.weather.network.weather.CurrentWeather
import com.iconsnotfound.weather.lib.WeatherUtils
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.direction_colon
import weather.composeapp.generated.resources.gust_colon
import weather.composeapp.generated.resources.hyphen
import weather.composeapp.generated.resources.km_per_h
import weather.composeapp.generated.resources.speed_colon
import weather.composeapp.generated.resources.turbine_bottom3
import weather.composeapp.generated.resources.turbine_top4
import weather.composeapp.generated.resources.wind
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun WindSection(currentWeather: CurrentWeather?) {
    val hyphen = stringResource(Res.string.hyphen)
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().height(IntrinsicSize.Max)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardM3(
                style = CardStyle.Outlined,
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.offset(x = (-18).dp)
                        ){
                            Box (
                                modifier = Modifier.offset(x = (36).dp)
                            ) {
                                ImageViewM3(
                                    painter = painterResource(Res.drawable.turbine_bottom3),
                                    style = ImageStyle.Icon,
                                    autoIconSize = false,
                                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                                    size = 78.dp,
                                    modifier = Modifier.offset(y = (9).dp)
                                )
                                ImageViewM3Animated(
                                    painter = painterResource(Res.drawable.turbine_top4),
                                    style = ImageStyle.Icon,
                                    isRotating = true,
                                    rotationDurationMs = 10000,
                                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                                    size = 78.dp,
                                    autoIconSize = false,
                                    initialRotationVal = 45f,
                                    targetRotationVal = 405f
                                )
                            }

                            Box {
                                ImageViewM3(
                                    painter = painterResource(Res.drawable.turbine_bottom3),
                                    style = ImageStyle.Icon,
                                    autoIconSize = false,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    size = 96.dp,
                                    modifier = Modifier.offset(y = (0).dp)
                                )
                                ImageViewM3Animated(
                                    painter = painterResource(Res.drawable.turbine_top4),
                                    style = ImageStyle.Icon,
                                    isRotating = true,
                                    rotationDurationMs = 8000,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    size = 96.dp,
                                    autoIconSize = false,
                                    modifier = Modifier.offset(y = (-10).dp),
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.weight(0.5f).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        var windSpeed = hyphen
                        currentWeather?.wind_speed_10m?.let {
                            windSpeed = round(it).toInt().toString()
                        }
                        var gustWindSpeed = hyphen
                        currentWeather?.wind_gusts_10m?.let {
                            gustWindSpeed = round(it).toInt().toString()
                        }
                        var windDirection = hyphen
                        currentWeather?.wind_direction_10m?.let {
                            windDirection = WeatherUtils.degreesToDirection(it.roundToInt())
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ){
                            ScrollableTextBox(
                                stringResource(Res.string.wind),
                                textStyle = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(
                            modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth().fillMaxHeight().padding(top = 16.dp),
                        ) {
                            Row(
                                modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth().fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(stringResource(Res.string.speed_colon), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                }

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("$windSpeed ${stringResource(Res.string.km_per_h)}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth().fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(stringResource(Res.string.gust_colon), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                }

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("$gustWindSpeed ${stringResource(Res.string.km_per_h)}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth().fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.padding(end = 8.dp),
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(stringResource(Res.string.direction_colon), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                }

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    ScrollableTextBox(windDirection, textStyle = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}