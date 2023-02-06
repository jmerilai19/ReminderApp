package com.jmerilai19.reminderapp.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jmerilai19.reminderapp.ui.theme.CardColor

@Composable
fun Profile(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {

        ScaffoldWithBottomMenu(navController)

    }
}

@Composable
fun ScaffoldWithBottomMenu(navController: NavController) {
    Scaffold(bottomBar = {BottomBar(navController)}
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Log out", modifier = Modifier.clickable { navController.navigate("login") })
            Text(text = "Edit", modifier = Modifier.clickable {  })
        }
        //content area
        CreateInfo()
    }
}

@Composable
private fun CreateInfo() {
    Column(
        modifier = Modifier
            .padding(top=80.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        Text(
            color = Color.Blue,
            fontSize = 32.sp,
            style = MaterialTheme.typography.h4,
            text = "John Johnson",
            fontWeight = FontWeight.Bold
        )
        Text(
            fontSize = 20.sp,
            style = MaterialTheme.typography.h4,
            text = "john77"
        )
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(1) }
    BottomNavigation(elevation = 10.dp) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"")
        },
            label = { Text(text = "Home") },
            selected = (selectedIndex.value == 0),
            onClick = {
                navController.navigate("home")
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"")
        },
            label = { Text(text = "Profile") },
            selected = (selectedIndex.value == 1),
            onClick = {

            })
    }
}

