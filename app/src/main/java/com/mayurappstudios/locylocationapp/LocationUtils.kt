package com.mayurappstudios.locylocationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationUtils(val context: Context) {
    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun requestLocationUpdates(viewModel: LocationViewModel) {
        val locationCallbackObj = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val locationData = LocationData(it.latitude, it.longitude)
                    viewModel.updateLocation(locationData)
                }
            }
        }
        val locationRequestObj =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        _fusedLocationClient.requestLocationUpdates(
            locationRequestObj,
            locationCallbackObj,
            Looper.getMainLooper()
        )
    }

    fun hasLocationPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

}