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

package com.iconsnotfound.weather.screens.settingsScreen.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.iconsnotfound.weather.components.AppSettingsItem1
import com.iconsnotfound.weather.components.CardStyle
import com.iconsnotfound.weather.components.TwoColumnEqualSizeSection
import com.iconsnotfound.weather.openUrl
import com.iconsnotfound.weather.screens.licencescreen.LicenceScreen
import org.jetbrains.compose.resources.stringResource
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.forward_round
import weather.composeapp.generated.resources.licences
import weather.composeapp.generated.resources.licences_ext
import weather.composeapp.generated.resources.licences_round
import weather.composeapp.generated.resources.open_in_browser_round
import weather.composeapp.generated.resources.privacy_policy
import weather.composeapp.generated.resources.privacy_policy_ext
import weather.composeapp.generated.resources.privacy_policy_round
import weather.composeapp.generated.resources.url_privacy_policy

@Composable
fun PrivacyAndLicenceSection() {
    TwoColumnEqualSizeSection(
        sectionLeft = { sectionLeft() },
        sectionRight = { sectionRight() },
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(bottom = 8.dp)
    )
}

@Composable
private fun sectionLeft() {
    val privacyUrl = stringResource(Res.string.url_privacy_policy)
    AppSettingsItem1(
        cardStyle = CardStyle.Outlined,
        titleText = stringResource(Res.string.privacy_policy),
        subtitleText = stringResource(Res.string.privacy_policy_ext),
        icon = Res.drawable.privacy_policy_round,
        endIcon = Res.drawable.open_in_browser_round,
        cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        itemOnClick = { openUrl(privacyUrl) }
    )
}

@Composable
private fun sectionRight() {
    val navigator = LocalNavigator.currentOrThrow
    AppSettingsItem1(
        cardStyle = CardStyle.Filled,
        titleText = stringResource(Res.string.licences),
        subtitleText = stringResource(Res.string.licences_ext),
        icon = Res.drawable.licences_round,
        endIcon = Res.drawable.forward_round,
        itemOnClick = { navigator.push(LicenceScreen()) }
    )
}