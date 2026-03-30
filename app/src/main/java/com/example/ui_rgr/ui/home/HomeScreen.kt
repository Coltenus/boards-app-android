package com.example.ui_rgr.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui_rgr.viewmodel.MainViewModel
import com.example.ui_rgr.viewmodel.Screen

@Composable
fun HomeScreen(vm: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Boards App",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            "Ask questions, post ideas, and discover useful answers through community discussions.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 28.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("What you can do", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Browse boards without login, then sign in to create posts and manage your profile.",
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(
            onClick = { vm.navigateTo(Screen.Boards) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text("Browse Boards")
        }

        if (!vm.isLoggedIn.value) {
            Button(
                onClick = { vm.navigateTo(Screen.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 12.dp),
            ) {
                Text("Login")
            }

            OutlinedButton(
                onClick = { vm.navigateTo(Screen.Register) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 12.dp)
            ) {
                Text("Register")
            }
        } else {
            OutlinedButton(
                onClick = { vm.navigateTo(Screen.Profile) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 12.dp)
            ) {
                Text("Open Profile")
            }
        }
    }
}
