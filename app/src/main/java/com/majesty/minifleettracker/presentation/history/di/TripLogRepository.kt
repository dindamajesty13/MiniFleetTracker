package com.majesty.minifleettracker.presentation.history.di

import com.majesty.minifleettracker.dao.AppDatabase
import com.majesty.minifleettracker.presentation.history.data.TripLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripLogRepository @Inject constructor(private val appDatabase: AppDatabase) {
    private val tripLogDao = appDatabase.tripLogDao()

    fun getAllTripLogs(): Flow<List<TripLog>> = tripLogDao.getAllTripLogs()

    suspend fun insertTripLog(tripLog: TripLog) {
        tripLogDao.insertTripLog(tripLog)
    }
}

