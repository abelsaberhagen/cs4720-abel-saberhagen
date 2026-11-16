package edu.virginia.cs4720.myapplication

import android.content.Context
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.Room

@Database(entities = [AccelSample::class], version = 2, exportSchema = false)
abstract class StepsDatabase : RoomDatabase() {
    abstract fun stepsDao() : StepDao

    companion object {
        @Volatile
        private var Instance : StepsDatabase? = null

        fun getDatabase(context: Context): StepsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StepsDatabase::class.java, "step_counter_database")
                    .fallbackToDestructiveMigration(dropAllTables = true)  // schema changed from steps to x/y/z
                    .build()
                    .also { edu.virginia.cs4720.myapplication.StepsDatabase.Companion.Instance = it }
            }
        }

    }
}