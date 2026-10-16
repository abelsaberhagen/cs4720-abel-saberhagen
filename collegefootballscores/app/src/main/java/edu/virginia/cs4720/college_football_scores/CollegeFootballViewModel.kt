package edu.virginia.cs4720.college_football_scores
import android.os.Message
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    data class Success(val data: FootballData) : CollegeFootballUiState
    object Loading : CollegeFootballUiState
    data class Error(val message: String) : CollegeFootballUiState
}

private val SEASON_START: LocalDate = LocalDate.of(2026, 9, 1)

class CollegeFootballViewModel : ViewModel() {

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
    private fun getFootballData() {
        viewModelScope.launch {
            uiState = CollegeFootballUiState.Loading
            val settings = _userUiState.value
            try {
               val listResult = CollegeFootballApi.retrofitService.getPosts(
                   conference = settings.conference,
                   week = settings.week
               );
                uiState = CollegeFootballUiState.Success(listResult);
            } catch (e:Exception) {
                uiState = CollegeFootballUiState.Error("Could not fetch data");
            }
        }
    }
}
