package com.jmerilai19.reminderapp.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.jmerilai19.reminderapp.ui.home.BottomBar
import com.jmerilai19.reminderapp.ui.home.TopBar
import kotlinx.coroutines.launch
import java.util.*
import com.jmerilai19.reminderapp.utils.rememberMapViewWithLifecycle

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReminderLocationMap(
    navController: NavController,
    context: Context
) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
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
        },
        topBar = {
            TopAppBar {
                IconButton(
                    onClick = {navController.popBackStack()}
                )
                {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(bottom = 36.dp)
        ) {
            AndroidView({ mapView }) { mapView ->
                coroutineScope.launch {
                    val map = mapView.awaitMap()
                    map.uiSettings.isZoomControlsEnabled = true
                    val location = LatLng(65.06, 25.47)

                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(location, 10f)
                    )

                    setMapClick(map = map, navController = navController, context)
                }
            }
        }
    }
}

private fun setMapClick(
    map: GoogleMap,
    navController: NavController,
    context: Context
) {
    map.setOnMapClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )

        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data", latlng)
            navController.popBackStack()
        }
    }
}