package com.jmerilai19.reminderapp.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.ListenableWorker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.jmerilai19.reminderapp.data.*
import com.jmerilai19.reminderapp.ui.home.BottomBar
import com.jmerilai19.reminderapp.ui.home.TopBar
import com.jmerilai19.reminderapp.ui.reminder.ReminderNotificationService
import kotlinx.coroutines.launch
import java.util.*
import com.jmerilai19.reminderapp.utils.rememberMapViewWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReminderLocationLookUpMap(
    navController: NavController,
    context: Context
) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val mReminderViewModel: ReminderViewModel = viewModel()

    Scaffold(
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

                    val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                    val manualLocationLat = sharedPreferences.getFloat("MANUAL_LOCATION_LAT", 65.06f).toDouble()
                    val manualLocationLng = sharedPreferences.getFloat("MANUAL_LOCATION_LNG", 25.47f).toDouble()
                    val manualLocation = LatLng(manualLocationLat, manualLocationLng)

                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(manualLocation, 14f)
                    )

                    val markerOptions = MarkerOptions()
                        .position(manualLocation)
                    var searchMarker = map.addMarker(markerOptions)

                    if (searchMarker != null) {
                        setMapClick(map = map, navController = navController, mReminderViewModel, context)
                    }
                }
            }


        }
    }
}

private fun setMapClick(
    map: GoogleMap,
    navController: NavController,
    vm: ReminderViewModel,
    context: Context
) {
    map.setOnMapClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )

        map.clear()

        val markerOptions = MarkerOptions()
            .position(LatLng(latlng.latitude, latlng.longitude))
        map.addMarker(markerOptions)

        saveManualLocation(context, latlng)

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            val list = withContext(Dispatchers.IO) {
                getList(latlng, vm)
            }

            updateMap(list, map, vm, context)
        }
    }
}

fun updateMap(list: List<Reminder>, map: GoogleMap, viewModel: ReminderViewModel, context: Context) {

    for (r in list) {
        val ll = LatLng(r.location_x, r.location_y)

        map.addMarker(
            MarkerOptions().position(ll).title(r.message)
        )

        val service = ReminderNotificationService(context)
        service.showNotification(r.message)

        viewModel.updateMessageSeenById(r.id)
    }
}

fun getList(latLng: LatLng, vm: ReminderViewModel): List<Reminder> {
    return vm.getRemindersNearLocation(latLng.latitude, latLng.longitude)
}

private fun saveManualLocation(context: Context, location: LatLng) {
    val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putFloat("MANUAL_LOCATION_LAT", location.latitude.toFloat())
    editor.putFloat("MANUAL_LOCATION_LNG", location.longitude.toFloat())
    editor.apply()
}
