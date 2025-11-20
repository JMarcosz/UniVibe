package com.example.univibe.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.base.Navigation.NavRoute
import com.example.univibe.base.Navigation.NavigationManager
import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.use_case.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    companion object { private const val TAG = "HomeViewModel" }

    fun onSignOutClick() {
        viewModelScope.launch {
            Log.d(TAG, "onSignOutClick: calling signOutUseCase")
            val result = signOutUseCase()
            when (result) {
                is AuthResult.Unauthenticated -> {
                    Log.d(TAG, "onSignOutClick: sign out successful, navigating to auth via NavigationManager")
                    NavigationManager.navigateTo(NavRoute.Auth)
                }
                is AuthResult.Error -> {
                    Log.d(TAG, "onSignOutClick: sign out error: ${result.message}")
                }
                else -> {
                    Log.d(TAG, "onSignOutClick: unexpected sign out result: $result")
                }
            }
        }
    }
}
