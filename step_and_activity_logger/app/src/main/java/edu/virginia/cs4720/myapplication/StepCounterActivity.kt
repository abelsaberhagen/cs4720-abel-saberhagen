package edu.virginia.cs4720.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.virginia.cs4720.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class StepCounterActivity : ComponentActivity() {
    private val viewModel: StepLoggerViewModel by viewModels {
        viewModelFactory {
            initializer {
                val dao = StepsDatabase.getDatabase(applicationContext).stepsDao()
                StepLoggerViewModel(StepRepository(applicationContext, dao))   // ← this is where "repo" comes from
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        stepLoggerViewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTracking()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTracking()
    }
}

@Composable
fun HomeScreen(stepLoggerViewModel: StepLoggerViewModel, modifier: Modifier = Modifier) {
    val days by stepLoggerViewModel.stepsPerDay.collectAsState()
    val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val today = days.firstOrNull { it.day == todayKey }?.steps ?: 0

    Column(modifier.padding(16.dp)) {
        if (!stepLoggerViewModel.hasAccelerometer) {
            Text("This device has no accelerometer.")
            return@Column
        }

        Text("Today", style = MaterialTheme.typography.titleMedium)
        Text("$today steps", style = MaterialTheme.typography.displayMedium)

        Spacer(Modifier.height(24.dp))
        Text("History", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(days, key = { it.day }) { d ->
                Text("${d.day}:  ${d.steps} steps")
            }
        }
    }
}

