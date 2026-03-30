package com.example.ui_rgr.ui.register

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui_rgr.viewmodel.MainViewModel
import com.example.ui_rgr.viewmodel.Screen
import java.util.Calendar
import java.util.Locale

@Composable
fun RegisterScreen(vm: MainViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    fun openBirthdatePicker() {
        val calendar = Calendar.getInstance()
        val dateParts = birthdate.split("-")
        val initialYear = dateParts.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)
        val initialMonth = (dateParts.getOrNull(1)?.toIntOrNull()?.minus(1)) ?: calendar.get(Calendar.MONTH)
        val initialDay = dateParts.getOrNull(2)?.toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                birthdate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            },
            initialYear,
            initialMonth,
            initialDay
        ).show()
    }

    fun submitRegister() {
        focusManager.clearFocus()
        vm.register(name, email, password, confirmPassword, gender, birthdate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Join to post boards, vote, and comment.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val error = vm.errorMessage.value
                if (error != null) {
                    Text(
                        error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    enabled = !vm.isLoading.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    enabled = !vm.isLoading.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedButton(
                        onClick = { genderExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !vm.isLoading.value
                    ) {
                        Text(gender.ifEmpty { "Gender" })
                    }

                    DropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("male", "female", "other").forEach { genderOption ->
                            DropdownMenuItem(
                                text = { Text(genderOption) },
                                onClick = {
                                    gender = genderOption
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = { openBirthdatePicker() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    enabled = !vm.isLoading.value
                ) {
                    Text(birthdate.ifEmpty { "Select Birthdate" })
                }

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    enabled = !vm.isLoading.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    singleLine = true,
                    enabled = !vm.isLoading.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submitRegister() })
                )

                Button(
                    onClick = {
                        submitRegister()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !vm.isLoading.value
                ) {
                    if (vm.isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Register")
                    }
                }

                TextButton(
                    onClick = { vm.navigateTo(Screen.Login) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Already have an account? Login")
                }

                TextButton(
                    onClick = { vm.navigateTo(Screen.Home) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}
