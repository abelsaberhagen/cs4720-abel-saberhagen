package edu.virginia.cs4720.college_football_scores

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Path


private const val BASE_URL = "https://ncaa-api.henrygd.me/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface CollegeFootballApiService {
    @GET("scoreboard/football/{conference}/2026/{week}/")
    suspend fun getPosts(
        @Path("conference") conference: String,
        @Path("week") week: String
    ): FootballData
}

object CollegeFootballApi {
    val retrofitService : CollegeFootballApiService by lazy {
        retrofit.create(CollegeFootballApiService::class.java)
    }
}