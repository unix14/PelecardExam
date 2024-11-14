package com.eyal.exam.pelecard.ui.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository
) : ViewModel() {


    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState


    fun onFinishedClicked() {
        viewModelScope.launch {
            // todo: save the payment details to the db
            delay(1250) /// todo use a ui state to show a loading spinner
            goToMainScreen()
        }
    }

    fun goToMainScreen() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }



}