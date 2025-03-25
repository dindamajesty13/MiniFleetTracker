package com.majesty.minifleettracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.majesty.minifleettracker.presentation.history.data.TripLog
import kotlinx.coroutines.flow.Flow

@Dao
interface TripLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripLog(tripLog: TripLog)

    @Query("SELECT * FROM trip_logs ORDER BY timestamp DESC")
    fun getAllTripLogs(): Flow<List<TripLog>>
}