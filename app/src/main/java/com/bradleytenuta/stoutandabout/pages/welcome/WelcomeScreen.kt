package com.bradleytenuta.stoutandabout.pages.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.models.PuckModel
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseParchment
import kotlinx.coroutines.launch

class WelcomeScreen {
    @Composable
    fun Content(
        locationPermissionGranted: Boolean,
        onGrantPermission: () -> Unit,
        onCharacterSelected: (PuckModel) -> Unit,
        onComplete: () -> Unit
    ) {
        val pagerState = rememberPagerState(pageCount = { 2 })
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RubberHoseParchment),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> LocationPermissionPage().Content(
                        locationPermissionGranted = locationPermissionGranted,
                        onGrantPermission = {
                            if (!locationPermissionGranted) {
                                onGrantPermission()
                            }
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                    1 -> CharacterSelectionPage().Content(
                        onCharacterSelected = onCharacterSelected,
                        onComplete = {
                            if (locationPermissionGranted) {
                                onComplete()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        }
                    )
                }
            }

            // Page Indicator
            Row(
                Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        RubberHoseBlack
                    } else {
                        RubberHoseBlack.copy(alpha = 0.2f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(10.dp)
                            .background(color, MaterialTheme.shapes.small)
                    )
                }
            }
        }
    }
}
