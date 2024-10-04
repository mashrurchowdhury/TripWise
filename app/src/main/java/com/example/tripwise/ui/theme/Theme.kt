/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tripwise.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Yellow,
    secondary = MintGreen,
    tertiary = Coral,
    secondaryContainer = Yellow,
    surface = White
)
private val DarkColorScheme = darkColorScheme(
    primary = DarkYellow,
    secondary = DarkMintGreen,
    tertiary = DarkCoral,
    secondaryContainer = DarkYellow,
    surface = Black
)

data class TripwiseExtraColors(
    val header: Color = Color.Unspecified,
    val cardBackground: Color = Color.Unspecified,
    val bed: Color = Color.Unspecified,
    val sleep: Color = Color.Unspecified,
    val wellness: Color = Color.Unspecified,
    val heart: Color = Color.Unspecified,
    val heartWave: List<Color> = listOf(Color.Unspecified),
    val heartWaveBackground: Color = Color.Unspecified,
    val sleepChartPrimary: Color = Color.Unspecified,
    val sleepChartSecondary: Color = Color.Unspecified,
    val sleepAwake: Color = Color.Unspecified,
    val sleepRem: Color = Color.Unspecified,
    val sleepLight: Color = Color.Unspecified,
    val sleepDeep: Color = Color.Unspecified,
)
val LocalExtraColors = staticCompositionLocalOf {
    TripwiseExtraColors()
}
private val LightExtraColors = TripwiseExtraColors(
    header = Yellow,
    cardBackground = White,
    bed = Lilac,
    sleep = MintGreen,
    wellness = LightBlue,
    heart = Coral,
    heartWave = listOf(Pink, Purple, Green),
    heartWaveBackground = Coral.copy(alpha = 0.2f),
    sleepChartPrimary = Yellow,
    sleepChartSecondary = YellowVariant,
    sleepAwake = SleepAwake,
    sleepRem = SleepRem,
    sleepLight = SleepLight,
    sleepDeep = SleepDeep,
)
private val DarkExtraColors = TripwiseExtraColors(
    header = DarkYellow,
    cardBackground = Black,
    bed = DarkLilac,
    sleep = DarkMintGreen,
    wellness = DarkBlue,
    heart = DarkCoral,
    heartWave = listOf(DarkPink, DarkPurple, DarkGreen),
    heartWaveBackground = DarkCoral.copy(alpha = 0.4f),
    sleepChartPrimary = DarkYellow,
    sleepChartSecondary = DarkYellowVariant,
    sleepAwake = SleepAwakeDark,
    sleepRem = SleepRemDark,
    sleepLight = SleepLightDark,
    sleepDeep = SleepDeepDark,
)

private val shapes: Shapes
    @Composable
    get() = MaterialTheme.shapes.copy(
        large = CircleShape
    )
@Composable
fun TripwiseTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme: ColorScheme
    val extraColors: TripwiseExtraColors
    if (isDarkTheme) {
        colorScheme = DarkColorScheme
        extraColors = DarkExtraColors
    } else {
        colorScheme = LightColorScheme
        extraColors = LightExtraColors
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = shapes,
            content = content
        )
    }
}

object Tripwiseheme {
    val extraColors: TripwiseExtraColors
        @Composable
        get() = LocalExtraColors.current
}