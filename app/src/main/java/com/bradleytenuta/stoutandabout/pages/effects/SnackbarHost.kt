package com.bradleytenuta.stoutandabout.pages.effects

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.R
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack

@Composable
fun BoxScope.SnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 100.dp) // Moved further up to avoid overlap with buttons
            .fillMaxWidth()
    ) { snackbarData ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            // The combined image (Character + Bubble)
            Box(
                modifier = Modifier
                    .size(width = 320.dp, height = 180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mr_stout_talking),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                // Text centered within the bubble area of the image
                // Adjusting horizontal and vertical padding to match the image's bubble
                Text(
                    text = snackbarData.visuals.message,
                    modifier = Modifier
                        .padding(bottom = 50.dp, end = 90.dp) // Moved up by 10 units
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = RubberHoseBlack,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
