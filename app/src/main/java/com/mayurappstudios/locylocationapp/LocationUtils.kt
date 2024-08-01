package com.mayurappstudios.locylocationapp

import android.content.Context
import androidx.core.content.ContextCompat

class LocationUtils(val context: Context) {
    fun hasLocationPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            return false
        }
    }
}