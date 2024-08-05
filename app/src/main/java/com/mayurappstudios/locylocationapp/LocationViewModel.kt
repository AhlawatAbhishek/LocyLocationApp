package com.mayurappstudios.locylocationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location
    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }
    fun updateAddress(newAddress: String){
        _location.value = _location.value?.copy(address = newAddress)
    }

}