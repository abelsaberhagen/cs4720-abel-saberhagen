package edu.virginia.cs4720.college_football_scores
import android.content.Context
import android.os.Message
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class UserUiState (
    val conference: String = "fbs",
    val week: String = "04",
)

sealed interface CollegeFootballUiState {
    data class Success(val data: List<GameEntity>) : CollegeFootballUiState
    object Loading : CollegeFootballUiState
    data class Error(val message: String) : CollegeFootballUiState
}

private val SEASON_START: LocalDate = LocalDate.of(2026, 9, 1)

class CollegeFootballViewModel(
    private val repository: FootballRepository
) : ViewModel() {

    companion object {
        // ViewModels can't get a Context themselves, so the Activity builds the repository and passes it in
        fun factory(context: Context) = viewModelFactory {
            initializer {
                val dao = FootballDatabase.getDatabase(context.applicationContext).gameDao()
                CollegeFootballViewModel(FootballRepository(dao))
            }
        }
    }

    private val today: LocalDate = LocalDate.now()

    val currentDateFormatted: String =
        today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.getDefault()))

    val currentWeek: Int =
        (ChronoUnit.DAYS.between(SEASON_START, today) / 7 + 1).toInt()

    private val _userUiState = MutableStateFlow(
        UserUiState(week = currentWeek.toString().padStart(2, '0'))
    )
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    var uiState : CollegeFootballUiState by mutableStateOf(CollegeFootballUiState.Loading)
        private set

    private var gamesJob: Job? = null

    init {
        getFootballData()
    }


    fun updateConference(chosenConference: String) {
        _userUiState.update { it.copy(conference = chosenConference) }
        getFootballData()
    }
    fun updateWeek(chosenWeek: String) {
        _userUiState.update { it.copy(week = chosenWeek) }
        getFootballData()
    }
    public fun getFootballData() {
        // Stop watching the previous conference/week before starting on the new one
        gamesJob?.cancel()
        gamesJob = viewModelScope.launch {
            uiState = CollegeFootballUiState.Loading
            val settings = _userUiState.value
            var refreshFailed = false
            try {
                repository.refresh(settings.conference, settings.week);
            } catch (e:Exception) {
                refreshFailed = true
            }
            repository.observeGames(settings.conference, settings.week).collect { games ->
                uiState = if (games.isEmpty() && refreshFailed) {
                    CollegeFootballUiState.Error("Could not fetch data")
                } else {
                    CollegeFootballUiState.Success(games)
                }
            }
        }
    }
}
