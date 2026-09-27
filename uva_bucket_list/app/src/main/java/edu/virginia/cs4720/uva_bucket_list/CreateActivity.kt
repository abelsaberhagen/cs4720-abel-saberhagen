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

class CreateActivity : ComponentActivity() {
    private val viewModel: BucketListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 2. Safely observe the state flow from the viewmode

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // 3. Pass the list and the callback method signature
                AddActivity(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun AddActivity(modifier: Modifier) {
    val state = rememberTextFieldState()

    TextField(
        state = state,
        label = { Text("Enter Activity") },
        modifier = modifier
    )

    Row {
        Button(
            modifier = modifier,
            onClick = {
                viewModel.addItem(
                    itemName = "Go to the beach",
                    dueDate = "2027-01-01"
                )
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


