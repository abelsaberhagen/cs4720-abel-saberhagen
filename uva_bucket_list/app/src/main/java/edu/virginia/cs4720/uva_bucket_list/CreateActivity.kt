package edu.virginia.cs4720.uva_bucket_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import kotlin.getValue
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            // 2. Safely observe the state flow from the viewmode

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // 3. Pass the list and the callback method signature
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

@Composable
fun AddActivity(
    modifier: Modifier,
    onAddItemClick: (String, String) -> Unit
    ) {
    val state = rememberTextFieldState()

    TextField(
        state = state,
        label = { Text("Enter Activity") },
        modifier = modifier
    )

    // 1. Control visibility of the dialog
    var showDatePicker by remember { mutableStateOf(false) }

    // 2. State to hold the picked date string (formatted as YYYY-MM-DD)
    var selectedDateStr by remember { mutableStateOf("Select Due Date") }

    // 3. Material 3 DatePicker State
    val datePickerState = rememberDatePickerState()
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        // Format the timestamp nicely into YYYY-MM-DD
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC") // Prevent timezone offset shift
                        }
                        selectedDateStr = formatter.format(Date(selectedMillis))
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


    Column {
        Button(
            modifier = modifier,
            onClick = {
                onAddItemClick("Go skiing", "2027-01-01")
            }
        ) {
            Text("Add Item")
        }

        val context = LocalContext.current
        Button(
            modifier = modifier,
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Cancel")
        }

    }
}


