package com.jmerilai19.reminderapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jmerilai19.reminderapp.ReminderAppState
import com.jmerilai19.reminderapp.rememberReminderAppState
import com.jmerilai19.reminderapp.ui.home.Home
import com.jmerilai19.reminderapp.ui.login.Login
import com.jmerilai19.reminderapp.ui.profile.Profile
import com.jmerilai19.reminderapp.ui.reminder.Reminder
import com.jmerilai19.reminderapp.ui.reminder.EditReminder

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
        composable(route = "reminder") {
            Reminder(navController = appState.navController)
        }
        composable(route = "edit_reminder/{id}/{message}",
                    arguments = listOf(
                        navArgument("id") {
                        type = NavType.IntType
                        },
                        navArgument("message") {
                            type = NavType.StringType
                        }
                    )
        ) { entry ->
            EditReminder(id = entry.arguments!!.getInt("id"), message = entry.arguments!!.getString("message"), navController = appState.navController)
        }
    }
}