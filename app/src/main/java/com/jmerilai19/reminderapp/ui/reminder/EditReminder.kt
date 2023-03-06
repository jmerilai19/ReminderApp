package com.jmerilai19.reminderapp.ui.reminder

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jmerilai19.reminderapp.data.Reminder
import com.jmerilai19.reminderapp.data.ReminderViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditReminder(
    navController: NavController,
    id: Int,
    message: String
) {
    val viewModel: ReminderViewModel = viewModel()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            val context = LocalContext.current
            val messageState = remember { mutableStateOf(message) }

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
                Text(text = "Edit Reminder",
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
                    onValueChange = { messageState.value = it },
                    label = { Text(text = "Message")},
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {IconButton(
                        onClick = {
                            if (permissionState.status.isGranted) {
                                speechRecognizerLauncher.launch(Unit)
                            } else {
                                permissionState.launchPermissionRequest()
                            }
                        }) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = "Speech-to-Text")
                    }}
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Location field
                OutlinedTextField(
                    value = "University of Oulu",
                    onValueChange = { },
                    label = { Text(text = "Location")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly) {

                    // Delete button
                    Button(
                        onClick = {
                            val myEntity = Reminder(id = id)
                            viewModel.deleteReminder(myEntity)
                            Toast.makeText(context,"Deleted reminder",Toast.LENGTH_SHORT).show();
                            navController.navigate("home")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red)
                    ) {
                        Text("Delete")
                    }

                    // Update button
                    Button(
                        onClick = {
                            val text = messageState.value
                            val myEntity = Reminder(id = id, message = text, location = "University of Oulu", creator_id = "john77")
                            viewModel.updateMessageById(id, text)
                            Toast.makeText(context,"Updated reminder",Toast.LENGTH_SHORT).show();
                            navController.navigate("home")
                        },
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}
