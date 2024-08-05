package com.mayurappstudios.locylocationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mayurappstudios.locylocationapp.ui.theme.LocyLocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocyLocationAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(modifier, locationUtils, context, viewModel)
}

@Composable
fun LocationDisplay(
    modifier: Modifier = Modifier,
    locationUtils: LocationUtils,
    context: Context,
    viewModel: LocationViewModel = LocationViewModel()
) {
    val location = viewModel.location.value
    val address = location?.let { locationUtils.reverseGeocodeLocation(it) }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    //Permission granted, update the location
                    Toast.makeText(context, "Fine Location permission Granted", Toast.LENGTH_SHORT)
                        .show()
                    locationUtils.requestLocationUpdates(viewModel)
                } else {
                    //Permission denied
                    val rationaleRequired = shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) || shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    if (rationaleRequired) {
                        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
                            Toast.makeText(
                                context,
                                "The app will work better on fine app location permission",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        else Toast.makeText(
                            context,
                            "Location Permission Denied. \nPlease allow from Android settings.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
                            Toast.makeText(
                                context,
                                "The app will work better on fine app location permission",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        else Toast.makeText(
                            context,
                            "Location Permission Denied. \nPlease allow from Android settings.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            })
    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        location?.let {
            Text("Latitude: ${it.latitude} Longitude: ${it.longitude} \n Address: $address", textAlign = TextAlign.Center)
        } ?: Text("Location not available")
        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)) {
                //Permission already granted, update the location
                locationUtils.requestLocationUpdates(viewModel)
            } else {
                //Request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text("Get Location")
        }
    }
}
