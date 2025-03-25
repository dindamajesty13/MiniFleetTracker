package com.majesty.minifleettracker.presentation.main.di

import com.google.android.gms.maps.model.LatLng
import com.majesty.minifleettracker.presentation.main.data.VehicleState
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainRepository @Inject constructor() {
    private val _vehicleState = MutableStateFlow(
        VehicleState(0f, false, false, LatLng(-6.8735543, 107.5846048))
    )

    private val route = listOf(
        LatLng(-6.8735543, 107.5846048),
        LatLng(-6.873813, 107.5845205),
        LatLng(-6.8743648, 107.5844795),
        LatLng(-6.8746394, 107.5844598),
        LatLng(-6.8748188, 107.5844422),
        LatLng(-6.8749968, 107.5844103),
        LatLng(-6.8752687, 107.5843682),
        LatLng(-6.8755395, 107.5843255),
        LatLng(-6.875722, 107.5843),
        LatLng(-6.8760748, 107.5842311),
        LatLng(-6.8764993, 107.584106),
        LatLng(-6.8768423, 107.5840783),
        LatLng(-6.8772985, 107.5840854),
        LatLng(-6.8776612, 107.5840469),
        LatLng(-6.8782362, 107.5840292),
        LatLng(-6.8790787, 107.5841762),
        LatLng(-6.8790101, 107.5843977),
        LatLng(-6.878933, 107.5846542),
        LatLng(-6.8788305, 107.5850019),
        LatLng(-6.8786981, 107.5854373),
        LatLng(-6.8785951, 107.5857877),
        LatLng(-6.8785018, 107.5860987),
        LatLng(-6.8783998, 107.5864558),
        LatLng(-6.8783516, 107.5866344),
        LatLng(-6.8782751, 107.5869034),
        LatLng(-6.8781227, 107.5874645),
        LatLng(-6.8780092, 107.587954),
        LatLng(-6.8778815, 107.5885009),
        LatLng(-6.8777511, 107.5891024),
    )

    private var routeIndex = 0
    private var simulationJob: Job? = null
    private val _isSimulationRunning = MutableStateFlow(false)
    val isSimulationRunning: StateFlow<Boolean> get() = _isSimulationRunning

    private fun getNextLocation(): LatLng {
        val newLocation = route[routeIndex]
        routeIndex = (routeIndex + 1) % route.size
        return newLocation
    }

    fun startSimulation() {
        if (_isSimulationRunning.value) return
        _isSimulationRunning.value = true

        simulationJob = CoroutineScope(Dispatchers.IO).launch {
            while (_isSimulationRunning.value) {
                val speed = (10..100).random().toFloat()
                val newState = VehicleState(
                    speed = speed,
                    engineOn = true,
                    doorOpen = listOf(true, false).random(),
                    location = getNextLocation()
                )
                _vehicleState.emit(newState)
                delay((5000 - (speed * 10)).coerceAtLeast(500.0f).toLong())
            }
        }
    }

    fun getVehicleState(): StateFlow<VehicleState> = _vehicleState
    fun getRoute(): List<LatLng> = route

    fun stopSimulation() {
        _isSimulationRunning.value = false
        simulationJob?.cancel()

        CoroutineScope(Dispatchers.IO).launch {
            _vehicleState.emit(
                _vehicleState.value.copy(
                    speed = 0f,
                    engineOn = false,
                    doorOpen = listOf(true, false).random(),
                )
            )
        }
    }
}
