package com.example.pitkiot.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameCreationJson(
    @Json(name = "nickName")
    val nickName: String
)

@JsonClass(generateAdapter = true)
data class GameCreationResponse(
    @Json(name = "gameId")
    val gameId: String
)

@JsonClass(generateAdapter = true)
data class PlayerAdderJson(
    @Json(name = "nickName")
    val nickName: String
)

@JsonClass(generateAdapter = true)
data class PlayersGetterResponse(
    @Json(name = "players")
    val players: List<String>
)

@JsonClass(generateAdapter = true)
data class WordAdderJson(
    @Json(name = "word")
    val word: String
)

@JsonClass(generateAdapter = true)
data class WordsGetterResponse(
    @Json(name = "words")
    val words: List<String>
)

@JsonClass(generateAdapter = true)
data class StatusGetterResponse(
    @Json(name = "status")
    val status: String
)

@JsonClass(generateAdapter = true)
data class StatusSetterJson(
    @Json(name = "status")
    val status: String
)