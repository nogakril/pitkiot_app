package com.example.pitkiot.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pitkiot.data.PitkiotRepository
import com.example.pitkiot.data.enums.GameStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AdminWaitingRoomUiState(
    val gamePin: String? = null,
    val players: List<String> = emptyList(),
    val gameStatus: GameStatus = GameStatus.GAME_CREATION,
    val errorMessage: String? = null
)

class AdminWaitingRoomViewModel(
    private val pitkiotRepository: PitkiotRepository,
    private val gamePin: String
) : ViewModel() {

    private var checkGameStatusJob: Job? = null
    private var getPlayersJob: Job? = null

    private val _uiState = MutableLiveData<AdminWaitingRoomUiState>()
    val uiState: LiveData<AdminWaitingRoomUiState> = _uiState

    init {
        _uiState.postValue(AdminWaitingRoomUiState())
    }

    fun getPlayers() {
        getPlayersJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                pitkiotRepository.getPlayers(gamePin).onSuccess { result ->
                    _uiState.let {
                        it.postValue(it.value!!.copy(players = result.players))
                    }
                }
                    .onFailure {
                        _uiState.let {
                            it.postValue(it.value!!.copy(errorMessage = "Error fetching players of game $gamePin"))
                        }
                    }
            }
        }
    }

    fun setGameStatus(status: GameStatus) {
        viewModelScope.launch {
            pitkiotRepository.setStatus(gamePin).onSuccess {
                _uiState.let {
                    it.postValue(it.value!!.copy(gameStatus = status))
                }
            }
                .onFailure {
                    _uiState.let {
                        it.postValue(it.value!!.copy(errorMessage = "Error setting game $gamePin status to $status"))
                    }
                }
        }
    }

    fun checkGameStatus() {
        checkGameStatusJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                pitkiotRepository.getStatus(gamePin).onSuccess { result ->
                    _uiState.let {
                        it.postValue(it.value!!.copy(gameStatus = result.status))
                    }
                }
                    .onFailure {
                        _uiState.let {
                            it.postValue(it.value!!.copy(errorMessage = "Error getting game status of game $gamePin"))
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        checkGameStatusJob?.cancel()
        getPlayersJob?.cancel()
    }
}