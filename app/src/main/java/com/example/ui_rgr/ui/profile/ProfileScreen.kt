package com.example.ui_rgr.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui_rgr.viewmodel.MainViewModel

@Composable
fun ProfileScreen(vm: MainViewModel) {
    LaunchedEffect(Unit) {
        vm.loadProfile()
    }

    val profile = vm.profile.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Account summary",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (profile == null) {
                    Text("Loading profile...")
                } else {
                    Text("Name", style = MaterialTheme.typography.labelMedium)
                    Text(profile.name, modifier = Modifier.padding(bottom = 10.dp))
                    Text("Email", style = MaterialTheme.typography.labelMedium)
                    Text(profile.email, modifier = Modifier.padding(bottom = 10.dp))
                    Text("Gender", style = MaterialTheme.typography.labelMedium)
                    Text(profile.gender, modifier = Modifier.padding(bottom = 10.dp))
                    Text("Birthdate", style = MaterialTheme.typography.labelMedium)
                    Text(profile.birthdate, modifier = Modifier.padding(bottom = 10.dp))
                    Text("Joined", style = MaterialTheme.typography.labelMedium)
                    Text(profile.joined)
                }
            }
        }
    }
}
