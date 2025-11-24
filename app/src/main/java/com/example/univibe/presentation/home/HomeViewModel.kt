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
//    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

}
