package com.example.proyectoaplicativo.presentation.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UIEventBus @Inject constructor(){
    private val _events = MutableSharedFlow<UIEvent>()
    val events = _events.asSharedFlow()

    suspend fun SendEvent(event: UIEvent){
        _events.emit(event)
    }
}