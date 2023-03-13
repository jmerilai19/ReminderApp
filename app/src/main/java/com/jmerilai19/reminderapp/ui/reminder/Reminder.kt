package com.jmerilai19.reminderapp.ui.reminder

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.insets.systemBarsPadding
import com.jmerilai19.reminderapp.data.Reminder
import com.jmerilai19.reminderapp.data.ReminderViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Reminder(
    navController: NavController,
    context: Context
) {
    val viewModel: ReminderViewModel = viewModel()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            val messageState = remember { mutableStateOf(viewModel.msg) } // MESSAGE
            var pickedDate by remember {                             // DATE
                mutableStateOf((LocalDate.now()))
            }
            var pickedTime by remember {                             // TIME
                mutableStateOf((LocalTime.NOON))
            }

            // Dropdown stuff
            var expanded by remember { mutableStateOf(false) }
            var expandedRepeat by remember { mutableStateOf(false) }
            var expandedWeek by remember { mutableStateOf(false) }
            var selectedType by remember { mutableStateOf(viewModel.mode) }
            var repeat by remember { mutableStateOf("Once") }
            var weekday by remember { mutableStateOf("Monday") }
            val types = listOf("Time", "Location", "Time and Location")
            val typesRepeat = listOf("Once", "Daily")
            val typesWeekday = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
            var textfieldSize by remember { mutableStateOf(Size.Zero)}

            val formattedDate by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("MMM dd yyyy")
                        .format(pickedDate)
                }
            }
            val formattedTime by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("HH:mm")
                        .format(pickedTime)
                }
            }

            val dateDialogState = rememberMaterialDialogState()
            val timeDialogState = rememberMaterialDialogState()

            var isNotificationsChecked by remember { mutableStateOf(true) }

            val latlng = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>("location_data")?.value

            // Speech-to-text stuff
            val permissionState = rememberPermissionState(
                permission = Manifest.permission.RECORD_AUDIO
            )
            SideEffect {
                permissionState.launchPermissionRequest()
            }

            val speechRecognizerLauncher = rememberLauncherForActivityResult(
                contract = SpeechRecognizerContract(),
                onResult = {
                    messageState.value = it.toString().drop(1).dropLast(1)
                }
            )

            TopAppBar {
                IconButton(
                    onClick = {navController.navigate("home")}
                )
                 {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "New Reminder",
                    modifier = Modifier
                        .padding(start = 10.dp))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {

                // Message field
                OutlinedTextField(
                    value = messageState.value,
                    onValueChange = {   messageState.value = it
                                        viewModel.msg = it
                                    },
                    label = { Text(text = "Message")},
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (permissionState.status.isGranted) {
                                    speechRecognizerLauncher.launch(Unit)
                                } else {
                                    permissionState.launchPermissionRequest()
                                }
                            } ) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = "Speech-to-Text")
                    }}
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Trigger field
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = { },
                            enabled = false,
                            label = { Text(text = "Trigger")},
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
                        ) {
                            types.forEach { item ->
                                DropdownMenuItem(onClick = {
                                    selectedType = item
                                    expanded = false
                                    viewModel.mode = selectedType
                                }) {
                                    Text(text = item)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                if (selectedType == "Time") {
                    // Repeat dropdown
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = repeat,
                            onValueChange = { },
                            enabled = false,
                            label = { Text(text = "Repeat")},
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            trailingIcon = {
                                IconButton(onClick = { expandedRepeat = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expandedRepeat,
                            onDismissRequest = { expandedRepeat = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
                        ) {
                            typesRepeat.forEach { item ->
                                DropdownMenuItem(onClick = {
                                    repeat = item
                                    expandedRepeat = false
                                }) {
                                    Text(text = item)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (selectedType == "Location" || selectedType == "Time and Location") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Location field
                        if (latlng == null) {
                            OutlinedTextField(
                                value = "",
                                onValueChange = { },
                                enabled = false,
                                label = { Text(text = "Location") },
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(color = Color.Black)
                            )
                        } else {
                            OutlinedTextField(
                                value = "Lat: ${latlng.latitude}, \nLng: ${latlng.longitude}",
                                onValueChange = { },
                                enabled = false,
                                label = { Text(text = "Location") },
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(color = Color.Black)
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate("map")
                            },
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        ) {
                            Text(text = "Choose")
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (selectedType == "Time and Location" || (selectedType == "Time" && repeat == "Once")) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Date field
                        OutlinedTextField(
                            value = formattedDate,
                            onValueChange = { },
                            enabled = false,
                            label = { Text(text = "Date") },
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                        Button(
                            onClick = {
                                dateDialogState.show()
                            },
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        ) {
                            Text(text = "Choose")
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }



                if (selectedType == "Time" || selectedType == "Time and Location") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Time field
                        OutlinedTextField(
                            value = formattedTime,
                            onValueChange = { },
                            enabled = false,
                            label = { Text(text = "Time") },
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black)
                        )
                        Button(
                            onClick = {
                                timeDialogState.show()
                            },
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        ) {
                            Text(text = "Choose")
                        }
                    }

                    MaterialDialog(
                        dialogState = dateDialogState,
                        buttons = {
                            positiveButton(text = "Ok") {
                                /* */
                            }
                            negativeButton(text = "Cancel")
                        }
                    ) {
                        datepicker(
                            initialDate = LocalDate.now(),
                            title = "Pick a date",
                            allowedDateValidator = {
                                it >= LocalDate.now()
                            }
                        ) {
                            pickedDate = it
                        }
                    }
                    MaterialDialog(
                        dialogState = timeDialogState,
                        buttons = {
                            positiveButton(text = "Ok") {
                                /* */
                            }
                            negativeButton(text = "Cancel")
                        }
                    ) {
                        timepicker(
                            is24HourClock = true,
                            initialTime = LocalTime.now(),
                            title = "Pick a time"
                        ) {
                            pickedTime = it
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                // Notification checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Checkbox(
                        modifier = Modifier
                            .scale(scale = 1.6f),
                        checked = isNotificationsChecked,
                        onCheckedChange = { isNotificationsChecked = it },
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "Notification"
                    )
                }

                // Submit button
                Button(
                    enabled = checkMessageLength(messageState.value),
                    onClick = {
                        val text = messageState.value
                        var dateTime: LocalDateTime = pickedDate.atTime(pickedTime)
                        var x: Boolean = false
                        var lat: Double = 0.0
                        var lng: Double = 0.0

                        var type = 0
                        if(selectedType == "Location") {
                            type = 1
                        } else if (selectedType == "Time and Location") {
                            type = 2
                        } else if (selectedType == "Time" && repeat == "Daily") {
                            type = 3
                            x = true
                            dateTime = LocalDate.now().atTime(pickedTime)
                        }

                        if (selectedType == "Location" || selectedType == "Time and Location") {
                            lat = latlng!!.latitude ?: 0.0
                            lng = latlng!!.longitude ?: 0.0
                        }

                        val myEntity = Reminder(id = 0,
                            message = text,
                            location = "University of Oulu",
                            creator_id = "john77",
                            location_x = lat,
                            location_y = lng,
                            reminder_datetime = dateTime,
                            creation_time = LocalDateTime.now(),
                            type = type,
                            notification_on = isNotificationsChecked
                        )

                        viewModel.addReminder(myEntity)

                        if (type == 0 || type == 2 || type == 3) {
                            scheduleNotification(context, dateTime, messageState.value, isNotificationsChecked, x)
                        }


                        Toast.makeText(context,"Reminder created",Toast.LENGTH_SHORT).show();
                        navController.navigate("home")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save reminder")
                }
            }
        }
    }
}

fun checkMessageLength(message: String): Boolean {
    if (message.isNotEmpty()) {
        return true
    }
    return false
}

fun scheduleNotification(context: Context, dateTime: LocalDateTime, title: String, notis: Boolean, daily: Boolean) {
    val currentTime = LocalDateTime.now()

    if (daily) {
        if (dateTime.isBefore(currentTime)) {
            dateTime.plusDays(1)
        }
    }
    val duration = Duration.between(currentTime, dateTime)
    val delay = duration.toMillis()

    val inputData = Data.Builder()
        .putString("title", title)
        .putString("dt", dateTime.toString())
        .putBoolean("notis", notis)
        .build()

    val notificationWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueue(notificationWorkRequest)
}
