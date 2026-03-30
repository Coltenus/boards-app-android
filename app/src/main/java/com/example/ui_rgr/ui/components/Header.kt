package com.example.ui_rgr.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui_rgr.viewmodel.MainViewModel
import com.example.ui_rgr.viewmodel.Screen

@Composable
fun Header(vm: MainViewModel) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = { vm.navigateTo(Screen.Home) }) {
                    Text("Home")
                }
                TextButton(onClick = { vm.navigateTo(Screen.Boards) }) {
                    Text("Boards")
                }
                if (vm.isLoggedIn.value) {
                    TextButton(onClick = { vm.navigateTo(Screen.Profile) }) {
                        Text("Profile")
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilledTonalIconButton(onClick = { vm.toggleDarkMode() }) {
                    Icon(
                        imageVector = if (vm.isDarkMode.value) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                        contentDescription = if (vm.isDarkMode.value) "Switch to light mode" else "Switch to dark mode"
                    )
                }

                if (vm.isLoggedIn.value) {
                    TextButton(
                        onClick = { vm.logout() }
                    ) {
                        Text("Logout")
                    }
                } else {
                    TextButton(
                        onClick = { vm.navigateTo(Screen.Login) }
                    ) {
                        Text("Login")
                    }
                    TextButton(onClick = { vm.navigateTo(Screen.Register) }) {
                        Text("Register")
                    }
                }
            }
        }
    }
}
