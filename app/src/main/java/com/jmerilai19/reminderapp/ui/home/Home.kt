package com.jmerilai19.reminderapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import com.jmerilai19.reminderapp.data.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController, context: Context) {

    val mReminderViewModel: ReminderViewModel = viewModel()

    val reminderList = mReminderViewModel.allReminders.observeAsState(listOf()).value // Get reminders
    val reminderSeenList = mReminderViewModel.allSeenReminders.observeAsState(listOf()).value // Get reminders
    val reminderUnseenList = mReminderViewModel.allUnseenReminders.observeAsState(listOf()).value // Get reminders

    val tabs = listOf("Occurred", "Scheduled", "All")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomBar(navController) },
            topBar = { TopBar()},
            floatingActionButton = {
                Box( modifier = Modifier.fillMaxSize().padding(start = 30.dp)) {
                    FloatingActionButton(
                        onClick = { navController.navigate("reminder") },
                        contentColor = Color.Blue,
                        modifier = Modifier.padding(all = 10.dp).align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("lookUpMap") },
                        contentColor = Color.Blue,
                        modifier = Modifier.padding(all = 10.dp).align(Alignment.BottomStart)
                    ) {
                        Text(text = "Map")
                    }
                }
            }
        ) {
            //content area
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 20.dp,
                modifier = Modifier.padding(top = 4.dp),
                backgroundColor = Color.Transparent,
                contentColor = Color.Black,
                divider = { },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                color = if (selectedTabIndex == index) Color.Black else Color.Gray
                            )
                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }

            var current: List<Reminder> = emptyList();
            if (selectedTabIndex == 0) {
                current = reminderSeenList
            } else if (selectedTabIndex == 1) {
                current = reminderUnseenList
            } else if (selectedTabIndex == 2) {
                current = reminderList
            }

            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(top = 54.dp, bottom = 58.dp), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(current) {
                    reminder ->
                    ReminderCard(navController = navController, reminder = reminder)
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
fun ReminderCard(navController: NavController, reminder: Reminder) {
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
                    text = reminder.message,
                    fontSize = 22.sp,
                    overflow = TextOverflow.Ellipsis
                )
                if(reminder.type == 1 || reminder.type == 2) { // "Location" or "Location and Time"
                    Text(
                        text = "Lat: ${reminder.location_x}\nLng: ${reminder.location_y}",
                        fontSize = 15.sp
                    )
                }
                if(reminder.type == 0 || reminder.type == 2) { // "Time" or "Location and Time"
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("dd MMM yyyy hh:mm")
                            .format(reminder.reminder_datetime),
                        fontSize = 15.sp
                    )
                } else if (reminder.type == 3) { // "Daily"
                    Text(
                        text = "Daily " + DateTimeFormatter
                            .ofPattern("hh:mm")
                            .format(reminder.reminder_datetime),
                        fontSize = 15.sp
                    )
                }
                if (reminder.notification_on) {
                    Text(
                        text = "Notification enabled",
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                Text(
                    text = "- john77",
                    fontSize = 15.sp
                )
            }
            IconButton(
                onClick = {
                    val id: Int = reminder.id
                    val message: String = reminder.message
                    navController.navigate("edit_reminder/$id/$message") }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}
