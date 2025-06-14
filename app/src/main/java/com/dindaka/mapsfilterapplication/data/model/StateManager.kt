package com.dindaka.mapsfilterapplication.data.model

sealed class StateManager<out T>{
    object Loading : StateManager<Nothing>()
    data class Success<out T>(val data: T) : StateManager<T>()
    data class Error(val message: String) : StateManager<Nothing>()
}