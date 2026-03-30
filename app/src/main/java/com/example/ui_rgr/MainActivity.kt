package com.example.ui_rgr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.ui_rgr.data.repository.MainRepository
import com.example.ui_rgr.ui.boards.BoardsScreen
import com.example.ui_rgr.ui.components.Header
import com.example.ui_rgr.ui.home.HomeScreen
import com.example.ui_rgr.ui.login.LoginScreen
import com.example.ui_rgr.ui.profile.ProfileScreen
import com.example.ui_rgr.ui.register.RegisterScreen
import com.example.ui_rgr.ui.theme.UI_RGRTheme
import com.example.ui_rgr.utils.SessionManager
import com.example.ui_rgr.viewmodel.MainViewModel
import com.example.ui_rgr.viewmodel.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        val vm = MainViewModel(MainRepository(), session)

        if (session.getToken().isNotEmpty()) {
            vm.isLoggedIn.value = true
            vm.currentScreen.value = Screen.Boards
        }

        setContent {
            UI_RGRTheme(darkTheme = vm.isDarkMode.value, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Header(vm)

                        when (vm.currentScreen.value) {
                            Screen.Home -> HomeScreen(vm)
                            Screen.Login -> LoginScreen(vm)
                            Screen.Register -> RegisterScreen(vm)
                            Screen.Boards -> {
                                LaunchedEffect(Unit) {
                                    if (vm.boards.value.isEmpty()) {
                                        vm.loadBoards()
                                    }
                                }
                                BoardsScreen(vm)
                            }
                            Screen.Profile -> ProfileScreen(vm)
                        }
                    }
                }
            }
        }
    }
}