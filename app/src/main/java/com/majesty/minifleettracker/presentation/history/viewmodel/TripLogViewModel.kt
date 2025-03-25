package com.majesty.minifleettracker.presentation.history.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.majesty.minifleettracker.presentation.history.data.TripLog
import com.majesty.minifleettracker.presentation.history.di.TripLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripLogViewModel @Inject constructor(
    private val repository: TripLogRepository
) : ViewModel() {

    val tripLogs: LiveData<List<TripLog>> = repository.getAllTripLogs().asLiveData()
}

