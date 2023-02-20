package com.jmerilai19.reminderapp.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        var visible = remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxHeight(),
            topBar = { TopBar() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Welcome!", fontSize = 30.sp)
                Spacer(modifier = Modifier.height(50.dp))
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { data -> username.value = data },
                    label = { Text("Username")},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { data -> password.value = data },
                    label = { Text("Password")},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(10.dp))
                if(visible.value) {
                    Text(text = "Wrong username or password!", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (username.value == "john77" && password.value == "password") {
                            navController.navigate("home")
                        } else {
                            visible.value = true
                        } },
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Login")
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
