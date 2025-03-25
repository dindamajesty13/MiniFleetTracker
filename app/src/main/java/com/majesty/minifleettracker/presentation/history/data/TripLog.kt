package com.majesty.minifleettracker.presentation.history.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_logs")
data class TripLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val speed: Float,
    val engineOn: Boolean,
    val doorOpen: Boolean
)
