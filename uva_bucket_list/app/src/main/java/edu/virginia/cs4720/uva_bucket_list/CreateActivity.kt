package edu.virginia.cs4720.uva_bucket_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.uva_bucket_list.ui.theme.Uva_bucket_listTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class CreateActivity : ComponentActivity() {
    private val viewModel: BucketListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Uva_bucket_listTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddActivity(
                        modifier = Modifier.padding(innerPadding),
                        onAddItemClick = { name, date ->
                            viewModel.addItem(name, date)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddActivity(
    modifier: Modifier,
    onAddItemClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val nameState = rememberTextFieldState()

    // Control visibility of the dialog
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    // The picked date, formatted as yyyy-MM-dd (null until the user picks one)
    var selectedDate by rememberSaveable { mutableStateOf<String?>(null) }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // DatePicker returns UTC midnight, so format in UTC to avoid an off-by-one day
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                        selectedDate = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        // Group the contents in the middle of the screen, both vertically and horizontally
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            state = nameState,
            label = { Text("Enter Activity") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDatePicker = true }
        ) {
            Text(selectedDate?.let { "Due: $it" } ?: "Select Due Date")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                enabled = nameState.text.isNotBlank() && selectedDate != null,
                onClick = {
                    onAddItemClick(nameState.text.toString(), selectedDate.toString())
                    val intent = Intent(context, MainActivity::class.java)
                    (context as? Activity)?.finish()
                }
            ) {
                Text("Add Item")
            }

            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    (context as? Activity)?.finish()
                }
            ) {
                Text("Cancel")
            }
        }
    }
}
