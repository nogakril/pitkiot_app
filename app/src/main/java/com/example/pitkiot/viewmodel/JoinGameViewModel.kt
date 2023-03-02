package com.example.pitkiot.viewmodel

/* ktlint-disable */
import androidx.lifecycle.*
/* ktlint-enable */
import com.example.pitkiot.data.PitkiotApi
import com.example.pitkiot.data.PitkiotRepositoryImpl
import com.example.pitkiot.data.models.JoinGameUiState
import kotlinx.coroutines.launch

class JoinGameViewModel(
    private val pitkiotRepositoryImpl: PitkiotRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableLiveData<JoinGameUiState>()
    val uiState: LiveData<JoinGameUiState> = _uiState

    init {
        _uiState.postValue(JoinGameUiState())
    }

    fun joinGame(gamePin: String, nickname: String) {
        val adminName = nickname.trimStart()
        if (adminName == "") {
            _uiState.postValue(_uiState.value!!.copy(errorMessage = "You must choose nickname to create the game"))
            return
        }
        viewModelScope.launch {
            pitkiotRepositoryImpl.addPlayer(gamePin, adminName).onSuccess { result ->
                _uiState.postValue(_uiState.value!!.copy(gamePin = gamePin))
            }
                .onFailure {
                    _uiState.postValue(_uiState.value!!.copy(errorMessage = "Error joining game $gamePin"))
                }
        }
    }

    class Factory(
        private val pitkiotRepositoryFactory: (PitkiotApi) -> PitkiotRepositoryImpl
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val pitkiotApi = PitkiotApi.instance
            return JoinGameViewModel(
                pitkiotRepositoryImpl = pitkiotRepositoryFactory.invoke(pitkiotApi)
            ) as T
        }
    }
}