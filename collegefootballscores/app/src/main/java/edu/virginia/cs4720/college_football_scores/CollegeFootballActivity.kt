package edu.virginia.cs4720.college_football_scores

import android.content.Intent
import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.college_football_scores.ui.theme.CollegefootballscoresTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.WatchEvent


class CollegeFootballActivity    : ComponentActivity() {
    val viewModel: CollegeFootballViewModel by viewModels {
        CollegeFootballViewModel.factory(this)
    }


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
                        collegeFootballViewModel = viewModel,
                        userUiState = userUiState,
                        onConferenceChange = { viewModel.updateConference(it) },
                        onWeekChange = { viewModel.updateWeek(it) },
                        onRefresh = { viewModel.getFootballData() },
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun HomeScreen(
    collegeFootballUiState: CollegeFootballUiState,
    collegeFootballViewModel: CollegeFootballViewModel,
    userUiState: UserUiState,
    onConferenceChange: (String) -> Unit,
    onWeekChange: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (collegeFootballUiState) {
        is CollegeFootballUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CollegeFootballUiState.Success -> ResultScreen(
            data = collegeFootballUiState.data,
            settings = userUiState,
            onConferenceChange = onConferenceChange,
            onWeekChange = onWeekChange,
            modifier = modifier.fillMaxWidth()
        )

        is CollegeFootballUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize(), onRefresh)
    }
}
@Composable
fun ResultScreen(
    data: List<GameEntity>,
    settings: UserUiState,
    onConferenceChange : (String) -> Unit,
    onWeekChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(vertical = 8.dp).background(MaterialTheme.colorScheme.background)) {
        if (data.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Games This Week",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        } else LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        TeamName(
                            name = item.awayName,
                            isFinal = item.finalMessage == "FINAL",
                            isWinner = item.awayWinner
                        )
                        Text(
                            text = "at",
                            modifier = Modifier
                        )
                        TeamName(
                            name = item.homeName,
                            isFinal = item.finalMessage == "FINAL",
                            isWinner = item.homeWinner
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = item.awayScore + " - " + item.homeScore,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = item.finalMessage
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {

                        if (item.finalMessage == "") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "Starts at " + item.startDate + " at " + item.startTime
                                )
                            }
                        }
                        else if (item.finalMessage != "FINAL") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = item.contestClock + " remaining"
                                )
                            }
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

// Highlights the winner and loser of finished games using the theme's container colors
@Composable
fun TeamName(name: String, isFinal: Boolean, isWinner: Boolean) {
    val (background, textColor) = when {
        !isFinal -> Color.Transparent to MaterialTheme.colorScheme.onBackground
        isWinner -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    Text(
        text = name,
        color = textColor,
        fontWeight = if (isFinal && isWinner) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
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
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
               onRefresh()
            },
        ) {
            Text(text="Refresh")
        }
    }
}

