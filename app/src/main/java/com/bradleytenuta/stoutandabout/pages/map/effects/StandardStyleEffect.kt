package com.bradleytenuta.stoutandabout.pages.map.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.rememberStandardStyleState
import java.util.Calendar

@Composable
fun StandardStyleEffect() {
    val lightPreset = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 6..8 -> LightPresetValue.DAWN
            in 9..17 -> LightPresetValue.DAY
            in 18..20 -> LightPresetValue.DUSK
            else -> LightPresetValue.NIGHT
        }
    }

    val standardStyleState = rememberStandardStyleState {
        configurationsState.lightPreset = lightPreset
        configurationsState.show3dObjects = BooleanValue(true)
    }

    MapboxStandardStyle(standardStyleState = standardStyleState)
}
