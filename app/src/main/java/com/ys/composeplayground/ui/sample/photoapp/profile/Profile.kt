package com.ys.composeplayground.ui.sample.photoapp.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.sample.photoapp.Photographer

@Composable
fun Profile(photographer: Photographer, modifier: Modifier = Modifier) {
    val padding = 16.dp
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        contentColor = MaterialTheme.colorScheme.surface,
    ) {
        TutorialOverlay { tutorialHighlightModifier ->
            Column(Modifier.padding(top = 24.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                ProfileHeader(
                    photographer = photographer,
                    tutorialHighlightModifier = tutorialHighlightModifier
                )
                Text(text = "Profile Header")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Tag List")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "PortfolioCard")
            }
        }
    }
}