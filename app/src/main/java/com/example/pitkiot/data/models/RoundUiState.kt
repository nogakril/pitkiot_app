package com.example.pitkiot.data.models

import com.example.pitkiot.data.enums.Team
import com.example.pitkiot.viewmodel.ROUND_TIME
import com.example.pitkiot.viewmodel.SKIPS

data class RoundUiState(
    var score: Int = 0,
    var skipsLeft: Int = SKIPS,
    var curWord: String = "",
    var timeLeftToRound: Long = ROUND_TIME,
    var curTeam: Team,
    var curPlayer: String,
    var gameEnded: Boolean = false,
    var teamAScore: Int = 0,
    var teamBScore: Int = 0,
    val errorMessage: String? = null
)