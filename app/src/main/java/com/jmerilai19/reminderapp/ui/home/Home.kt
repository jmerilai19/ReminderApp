package com.jmerilai19.reminderapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmerilai19.reminderapp.data.ReminderViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController) {

    val mReminderViewModel: ReminderViewModel = viewModel()

    val reminderList = mReminderViewModel.allReminders.observeAsState(listOf()).value // Get reminders

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomBar(navController) },
            topBar = { TopBar()},

            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("reminder") },
                    contentColor = Color.Blue,
                    modifier = Modifier.padding(all = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) {
            //content area
            LazyColumn(modifier = Modifier.fillMaxHeight(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(reminderList) {
                    reminder ->
                    ReminderCard(navController = navController, id = reminder.id, message = reminder.message)
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar {
        Text(
            text = "Reminder App",
            modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(elevation = 10.dp) {
        BottomNavigationItem(
            icon = {
                Icon(imageVector = Icons.Default.Home,"")
            },
            label = { Text(text = "Home") },
            selected = (selectedIndex.value == 0),
            onClick = { /* We are already in home activity*/ }
        )

        BottomNavigationItem(
            icon = {
                Icon(imageVector = Icons.Default.Person,"")
            },
            label = { Text(text = "Profile") },
            selected = (selectedIndex.value == 1),
            onClick = {
                navController.navigate("profile")
            }
        )
    }
}

@Composable
fun ReminderCard(navController: NavController, id: Int, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = 8.dp) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = message,
                    fontSize = 22.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "University of Oulu",
                    fontSize = 15.sp
                )
                Text(
                    text = "- john77",
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp
                )
            }
            IconButton(
                onClick = { navController.navigate("edit_reminder/$id/$message") }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}
