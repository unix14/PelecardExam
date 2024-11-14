package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.models.NavEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRepository @Inject constructor() {

    private val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    suspend fun navigateTo(event: NavEvent) {
        _navigationEvents.emit(event)
    }
}