package com.jmerilai19.reminderapp.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jmerilai19.reminderapp.ui.theme.CardColor

@Composable
fun Home(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithBottomMenu(navController)
    }
}

@Composable
fun ScaffoldWithBottomMenu(navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) },
        floatingActionButton = { FloatingActionButton(
            onClick = {  },
            contentColor = Color.Blue,
            modifier = Modifier.padding(all = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
    ) {
        //content area
        val mList = listOf(
            "17:28", "15:30", "13:37", "11:11", "7:00"
        )
        LazyColumn(modifier = Modifier.fillMaxHeight(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(mList) { item ->
                MessageCard("$item")
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(elevation = 10.dp) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"")
        },
            label = { Text(text = "Home") },
            selected = (selectedIndex.value == 0),
            onClick = {

            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"")
        },
            label = { Text(text = "Profile") },
            selected = (selectedIndex.value == 1),
            onClick = {
                navController.navigate("profile")
            })
    }
}

@Composable
fun MessageCard(m: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
        elevation = 8.dp) {
        Column(modifier = Modifier.padding(start = 15.dp, top = 15.dp)) {
            Text(text = "Do something!", fontSize = 26.sp)
            Text(text = "$m", fontSize = 17.sp)
        }
    }
}


