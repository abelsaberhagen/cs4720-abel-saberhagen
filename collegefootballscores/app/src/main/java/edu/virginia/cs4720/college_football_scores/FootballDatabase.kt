package edu.virginia.cs4720.college_football_scores

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun gameDao() : GameDao

    companion object {
        @Volatile
        private var Instance: FootballDatabase? = null

        fun getDatabase(context: Context): FootballDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FootballDatabase::class.java, "football_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
