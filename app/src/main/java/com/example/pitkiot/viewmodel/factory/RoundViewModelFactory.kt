package com.example.pitkiot.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pitkiot.data.PitkiotApi
import com.example.pitkiot.data.PitkiotRepository
import com.example.pitkiot.viewmodel.RoundViewModel

class RoundViewModelFactory(
    private val pitkiotRepositoryFactory: (PitkiotApi) -> PitkiotRepository,
    private val gamePinFactory: () -> String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val pitkiotApi = PitkiotApi.instance
        return RoundViewModel(
            gamePin = gamePinFactory.invoke(),
            pitkiotRepository = pitkiotRepositoryFactory.invoke(pitkiotApi)
        ) as T
    }
}