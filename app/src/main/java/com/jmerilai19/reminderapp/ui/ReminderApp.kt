package com.jmerilai19.reminderapp.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jmerilai19.reminderapp.ReminderAppState
import com.jmerilai19.reminderapp.data.ReminderViewModel
import com.jmerilai19.reminderapp.rememberReminderAppState
import com.jmerilai19.reminderapp.ui.home.Home
import com.jmerilai19.reminderapp.ui.login.Login
import com.jmerilai19.reminderapp.ui.profile.Profile
import com.jmerilai19.reminderapp.ui.reminder.Reminder
import com.jmerilai19.reminderapp.ui.reminder.EditReminder

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState(),
    context: Context
) {
    val viewModel: ReminderViewModel = viewModel()

    NavHost(
        navController = appState.navController,
        startDestination = "home"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "home") {
            viewModel.updateSeenLists()
            Home(navController = appState.navController, context = context)
        }
        composable(route = "profile") {
            Profile(navController = appState.navController)
        }
        composable(route = "reminder") {
            Reminder(navController = appState.navController, context = context)
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
            EditReminder(id = entry.arguments!!.getInt("id"), message = entry.arguments!!.getString("message").orEmpty(), navController = appState.navController)
        }
    }
}