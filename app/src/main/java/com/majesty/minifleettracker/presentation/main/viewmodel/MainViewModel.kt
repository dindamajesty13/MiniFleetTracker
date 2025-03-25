package com.majesty.minifleettracker.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.maps.model.LatLng
import com.majesty.minifleettracker.core.utils.NotificationWorker
import com.majesty.minifleettracker.presentation.history.data.TripLog
import com.majesty.minifleettracker.presentation.history.di.TripLogRepository
import com.majesty.minifleettracker.presentation.login.di.AuthRepository
import com.majesty.minifleettracker.presentation.main.data.VehicleState
import com.majesty.minifleettracker.presentation.main.di.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val tripLogRepository: TripLogRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val vehicleState: StateFlow<VehicleState> = repository.getVehicleState()
    val isSimulationRunning: StateFlow<Boolean> = repository.isSimulationRunning
    private val _alertMessage = MutableStateFlow<String?>(null)
    val route: List<LatLng> = repository.getRoute()

    private val _authState = MutableStateFlow<String?>(value = null)

    private val destination: LatLng = route.last()

    init {
        monitorVehicleState()
    }

    fun startSimulation() {
        repository.startSimulation()
    }

    private fun sendImmediateNotification(alertMessage: String) {
        val data = workDataOf("alert_message" to alertMessage)
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance().enqueue(workRequest)
    }

    private fun monitorVehicleState() {
        viewModelScope.launch {
            vehicleState.collectLatest { state ->
                val alerts = mutableListOf<String>()

                if (state.speed > 80) {
                    alerts.add("⚠️ Speeding! (${state.speed} km/h)")
                }
                if (state.doorOpen && state.speed > 0) {
                    alerts.add("🚪 Door open while moving!")
                }
                if (state.engineOn) {
                    alerts.add("🔧 Engine turned ON")
                } else {
                    alerts.add("⛔ Engine turned OFF")
                }

                if (isNearDestination(state.location, destination)) {
                    stopSimulation()
                    _alertMessage.value = "🏁 Reached destination!"
                    alerts.add("🏁 Reached destination!")
                }

                if (alerts.isNotEmpty()) {
                    val alertText = alerts.joinToString("\n")
                    _alertMessage.value = alertText
                    sendImmediateNotification(alertText)
                }

                logVehicleState(state)
            }
        }
    }

    private fun logVehicleState(state: VehicleState) {
        viewModelScope.launch {
            val tripLog = TripLog(
                timestamp = System.currentTimeMillis(),
                latitude = state.location.latitude,
                longitude = state.location.longitude,
                speed = state.speed,
                engineOn = state.engineOn,
                doorOpen = state.doorOpen
            )
            tripLogRepository.insertTripLog(tripLog)
        }
    }

    private fun isNearDestination(currentLocation: LatLng, destination: LatLng, threshold: Double = 0.0001): Boolean {
        return abs(currentLocation.latitude - destination.latitude) < threshold &&
                abs(currentLocation.longitude - destination.longitude) < threshold
    }

    fun stopSimulation() {
        repository.stopSimulation()
    }

    fun logout() {
        authRepository.logout()
        _authState.value = null
    }
}