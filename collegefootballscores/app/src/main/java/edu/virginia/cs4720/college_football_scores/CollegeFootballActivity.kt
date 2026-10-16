package edu.virginia.cs4720.college_football_scores

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.college_football_scores.ui.theme.CollegefootballscoresTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.WatchEvent


class CollegeFootballActivity    : ComponentActivity() {
    val viewModel: CollegeFootballViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegefootballscoresTheme {
                val collegeFootballState = viewModel.uiState
                val userUiState by viewModel.userUiState.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        collegeFootballState,
                        userUiState = userUiState,
                        onConferenceChange = { viewModel.updateConference(it) },
                        onWeekChange = { viewModel.updateWeek(it) },
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun HomeScreen(
    collegeFootballUiState: CollegeFootballUiState,
    userUiState: UserUiState,
    onConferenceChange: (String) -> Unit,
    onWeekChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (collegeFootballUiState) {
        is CollegeFootballUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CollegeFootballUiState.Success -> ResultScreen(
            data = collegeFootballUiState.data.games.map {it},
            settings = userUiState,
            onConferenceChange = onConferenceChange,
            onWeekChange = onWeekChange,
            modifier = modifier.fillMaxWidth()
        )

        is CollegeFootballUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}
@Composable
fun ResultScreen(
    data: List<GameEntry>,
    settings: UserUiState,
    onConferenceChange : (String) -> Unit,
    onWeekChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(vertical = 8.dp)) {
        LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
            items(data) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = item.game.away.names.short,
                        modifier = Modifier
                            .then(
                                if (item.game.away.winner) {
                                    Modifier.background(Color.Green)
                                } else {
                                    Modifier.background(Color.Red)
                                }
                            )
                    )
                    Text(
                        text = "at",
                        modifier = Modifier
                    )
                    Text(
                        text = item.game.home.names.short,
                        modifier = Modifier
                            .then(
                            if (item.game.home.winner) {
                                Modifier.background(Color.Green)
                            } else {
                                Modifier.background(Color.Red)
                            }
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = item.game.away.score + " - " + item.game.home.score
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = item.game.finalMessage
                    )
                    if (item.game.finalMessage == "") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Starts at " + item.game.startDate + " at " + item.game.startTime
                            )
                        }
                    }
                    else if (item.game.finalMessage != "FINAL") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = item.game.contestClock + " remaining"
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = {
                    onConferenceChange("fbs");
                },
                enabled = (settings.conference != "fbs")
            ) {
                Text("FB Conference")
            }
            Button(
                onClick = {
                    onConferenceChange("fcs")
                },
                enabled = (settings.conference != "fcs")
            ) {
                Text("FC Conference")
            }
        }
        val week = settings.week.toInt()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(52.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    onWeekChange((week - 1).toString().padStart(2, '0'))
                },
                enabled = week > 1
            ) {
                Text("-")
            }
            Text("Week $week")
            Button(
                onClick = {
                    onWeekChange((week + 1).toString().padStart(2, '0'))
                }
            ) {
                Text("+")
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image (
            painter = painterResource(id = R.drawable.loading_img), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

