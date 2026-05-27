package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

enum class AppScreen {
    Guide, Library, Relay
}

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf(AppScreen.Guide) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CosmicSurface,
                contentColor = CosmicTextSecondary,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Guide,
                    onClick = { currentScreen = AppScreen.Guide },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Guide") },
                    label = { Text("Flow Guide") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicGreen,
                        selectedTextColor = CosmicGreen,
                        indicatorColor = CosmicGreen.copy(alpha = 0.1f),
                        unselectedIconColor = CosmicTextTertiary,
                        unselectedTextColor = CosmicTextTertiary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Library,
                    onClick = { currentScreen = AppScreen.Library },
                    icon = { Icon(Icons.Default.AutoAwesomeMotion, contentDescription = "Library") },
                    label = { Text("Library") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicGreen,
                        selectedTextColor = CosmicGreen,
                        indicatorColor = CosmicGreen.copy(alpha = 0.1f),
                        unselectedIconColor = CosmicTextTertiary,
                        unselectedTextColor = CosmicTextTertiary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Relay,
                    onClick = { currentScreen = AppScreen.Relay },
                    icon = { Icon(Icons.Default.SyncAlt, contentDescription = "Relay") },
                    label = { Text("Relay") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicGreen,
                        selectedTextColor = CosmicGreen,
                        indicatorColor = CosmicGreen.copy(alpha = 0.1f),
                        unselectedIconColor = CosmicTextTertiary,
                        unselectedTextColor = CosmicTextTertiary
                    )
                )
            }
        },
        containerColor = CosmicBackground
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = Color.Transparent
        ) {
            when (currentScreen) {
                AppScreen.Guide -> SplitScreenGuideScreen()
                AppScreen.Library -> PromptLibraryScreen()
                AppScreen.Relay -> RelayScreen()
            }
        }
    }
}
