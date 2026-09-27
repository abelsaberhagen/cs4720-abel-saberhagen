package edu.virginia.cs4720.uva_bucket_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the item information sent from MainActivity
        val itemId = intent.getIntExtra("item_id", -1)

        val itemName = intent.getStringExtra("item_name") ?: ""
        val dueDate = intent.getStringExtra("due_date") ?: ""

        val completed =
            intent.getBooleanExtra("completed", false)

        val completedDate =
            intent.getStringExtra("completed_date")
        setContent {

            // 2. Safely observe the state flow from the viewmode

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // 3. Pass the list and the callback method signature
                DetailActivity(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun DetailActivity(modifier: Modifier) {

    Row {
        Button(
            modifier = modifier,
            onClick = {
                completed = false
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
