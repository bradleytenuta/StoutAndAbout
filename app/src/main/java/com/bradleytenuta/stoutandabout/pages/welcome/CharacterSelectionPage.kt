package com.bradleytenuta.stoutandabout.pages.welcome

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.domain.PuckModel
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseWhite

class CharacterSelectionPage {
    @Composable
    fun Content(
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
                            .alpha(if (selectedModel == model) 1.0f else 0.4f),
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
