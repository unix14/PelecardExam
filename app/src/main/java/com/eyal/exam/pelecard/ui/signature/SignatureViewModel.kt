package com.eyal.exam.pelecard.ui.signature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.repos.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignatureViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository
) : ViewModel() {
    fun goToNextScreen() {
        viewModelScope.launch {
//            navigationRepository.navigateTo(NavEvent.NavigateToReceipt()) // todo use the paymentDetails
        }
    }

    fun goToPreviousScreen() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

}