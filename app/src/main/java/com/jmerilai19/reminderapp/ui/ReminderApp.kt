package com.jmerilai19.reminderapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmerilai19.reminderapp.ReminderAppState
import com.jmerilai19.reminderapp.rememberReminderAppState
import com.jmerilai19.reminderapp.ui.home.Home
import com.jmerilai19.reminderapp.ui.login.Login
import com.jmerilai19.reminderapp.ui.profile.Profile

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "profile") {
            Profile(navController = appState.navController)
        }
    }
}