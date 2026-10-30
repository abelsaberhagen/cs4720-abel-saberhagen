package edu.virginia.cs4720.college_football_scores

import kotlinx.coroutines.flow.Flow

class FootballRepository(private val dao: GameDao) {
    fun observeGames(conference: String, week: String): Flow<List<GameEntity>> = dao.observeAllGames(conference = conference,week = week);
    suspend fun refresh(conference: String, week: String) {
        val listResult = CollegeFootballApi.retrofitService.getPosts(
            conference = conference,
            week = week
        );
        dao.upsert(listResult.toEntities(conference, week))
    };
}