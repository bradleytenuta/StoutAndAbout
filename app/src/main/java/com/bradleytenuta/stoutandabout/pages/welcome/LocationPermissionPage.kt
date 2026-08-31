package com.bradleytenuta.stoutandabout.pages.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack

class LocationPermissionPage {
    @Composable
    fun Content(
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
            if (!locationPermissionGranted) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "If you do not see a popup, please grant permission in your device settings.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = RubberHoseBlack,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
