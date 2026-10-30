package edu.virginia.cs4720.college_football_scores

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class GameEntity(
    @PrimaryKey val gameID: String,
    val homeName: String,
    val awayName: String,
    val homeScore: String,
    val awayScore: String,
    val homeWinner: Boolean,
    val awayWinner: Boolean,
    val finalMessage: String,
    val startDate: String,
    val startTime: String,
    val contestClock: String,
    val gameState: String,
    val conference: String,
    val week: String
)

// The API response doesn't include conference/week, so the caller passes in the ones it requested
fun Game.toEntity(conference: String, week: String) = GameEntity(
    gameID = gameID,
    homeName = home.names.short,
    awayName = away.names.short,
    homeScore = home.score,
    awayScore = away.score,
    homeWinner = home.winner,
    awayWinner = away.winner,
    finalMessage = finalMessage,
    startDate = startDate,
    startTime = startTime,
    contestClock = contestClock,
    gameState = gameState,
    conference = conference,
    week = week
)

fun FootballData.toEntities(conference: String, week: String): List<GameEntity> =
    games.map { it.game.toEntity(conference, week) }

@Dao
interface GameDao {
    @Query("SELECT * FROM GameEntity WHERE conference = :conference AND week = :week")
    fun observeAllGames(conference: String, week: String): Flow<List<GameEntity>>

    @Upsert
    suspend fun upsert(item: List<GameEntity>)
}
