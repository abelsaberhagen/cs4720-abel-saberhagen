package edu.virginia.cs4720.college_football_scores

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FootballData(
    val inputMD5Sum: String,
    val instanceId: String,
    @SerialName("updated_at")
    @SerializedName("updated_at")
    val updatedAt: String,
    val games: List<GameEntry>
)

@Serializable
data class GameEntry(
    val game: Game
)

@Serializable
data class Game(
    val gameID: String,
    val away: Team,
    val home: Team,
    val finalMessage: String,
    val title: String,
    val url: String,
    val network: String?,
    val liveVideoEnabled: Boolean,
    val startTime: String,
    val startTimeEpoch: String,
    val gameState: String,
    val startDate: String,
    val currentPeriod: String,
    val contestClock: String,
    val bracketId: String?,
    val bracketRound: String?
)

@Serializable
data class Team(
    val score: String,
    val names: TeamNames,
    val winner: Boolean,
    val seed: String?,
    val description: String,
    val rank: String?,
    val conferences: List<Conference>
)

@Serializable
data class TeamNames(
    val char6: String,
    val short: String,
    val seo: String,
    val full: String
)

@Serializable
data class Conference(
    val conferenceName: String,
    val conferenceSeo: String
)
