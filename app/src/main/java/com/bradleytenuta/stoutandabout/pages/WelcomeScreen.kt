package com.bradleytenuta.stoutandabout.pages

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.models.PuckModel
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseParchment
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseWhite
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
                    0 -> WelcomePage(
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
                    1 -> CharacterSelectionPage(onCharacterSelected, onComplete)
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

    @Composable
    private fun WelcomePage(
        locationPermissionGranted: Boolean,
        onGrantPermission: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Stout & About!",
                style = MaterialTheme.typography.headlineLarge,
                color = RubberHoseBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "To find the best pubs and track your visits, we need to know your location.",
                style = MaterialTheme.typography.bodyLarge,
                color = RubberHoseBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onGrantPermission) {
                if (locationPermissionGranted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Permission Granted",
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = "Grant Location Permission")
                }
            }
        }
    }

    @Composable
    private fun CharacterSelectionPage(
        onCharacterSelected: (PuckModel) -> Unit,
        onComplete: () -> Unit
    ) {
        var selectedModel by remember { mutableStateOf<PuckModel?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose Your Character",
                style = MaterialTheme.typography.headlineLarge,
                color = RubberHoseBlack,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // For now, just show the one option
            PuckModel.entries.forEach { model ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = {
                        selectedModel = model
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = RubberHoseWhite,
                        contentColor = RubberHoseBlack
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .alpha(if (selectedModel == model) 1.0f else 0.6f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val context = LocalContext.current
                        val bitmap = remember(model.previewImagePath) {
                            val inputStream = context.assets.open(model.previewImagePath)
                            BitmapFactory.decodeStream(inputStream).asImageBitmap()
                        }
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = model.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            color = RubberHoseBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    selectedModel?.let {
                        onCharacterSelected(it)
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedModel != null,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Start Exploring")
            }
        }
    }
}
