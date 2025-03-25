package com.majesty.minifleettracker.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.majesty.minifleettracker.presentation.login.di.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<String?>(value = null)
    val authState: StateFlow<String?> get() = _authState

    val currentUser: FirebaseUser? get() = authRepository.getCurrentUser()

    fun register(email: String, password: String) {
        authRepository.registerUser(email, password) { success, error ->
            _authState.value = if (success) "Success" else error
        }
    }

    fun login(email: String, password: String) {
        authRepository.loginUser(email, password) { success, error ->
            _authState.value = if (success) "Success" else error
        }
    }
}

