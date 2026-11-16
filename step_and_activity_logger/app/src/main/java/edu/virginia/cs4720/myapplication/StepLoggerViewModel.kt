package edu.virginia.cs4720.myapplication

import androidx.room3.Query
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


data class DailySteps(
    val day: String,
    val steps: Int
)
@Entity
data class AccelSample(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val x: Float,  // m/s², includes gravity
    val y: Float,
    val z: Float
)

@Dao
interface StepDao {
    @Insert suspend fun insert(s: AccelSample)
    @Query("SELECT * FROM AccelSample ORDER BY timestamp DESC")
    fun all(): Flow<List<AccelSample>>

    @Query("""
        SELECT date(timestamp / 1000, 'unixepoch', 'localtime') AS day,
               COUNT(*) AS steps
        FROM AccelSample
        GROUP BY day
        ORDER BY day DESC
    """)
    fun stepsPerDay(): Flow<List<DailySteps>>
}

// UI side: reads the database only
class StepLoggerViewModel(private val repo: StepRepository) : ViewModel() {
    val samples = repo.samples.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val currentReading = repo.currentReading
    val stepsPerDay = repo.stepsPerDay.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hasAccelerometer = repo.hasAccelerometer
    fun startTracking() = repo.start()
    fun stopTracking() = repo.stop()

    override fun onCleared() = repo.stop()

}
