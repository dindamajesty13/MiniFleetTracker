package com.majesty.minifleettracker.presentation.main.data

import com.google.android.gms.maps.model.LatLng

data class VehicleState(
    val speed: Float,
    val engineOn: Boolean,
    val doorOpen: Boolean,
    val location: LatLng
)