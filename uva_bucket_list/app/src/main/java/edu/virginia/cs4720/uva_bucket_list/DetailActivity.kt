package edu.virginia.cs4720.uva_bucket_list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.uva_bucket_list.ui.theme.Uva_bucket_listTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlin.getValue


class DetailActivity : ComponentActivity() {

    private val viewModel: BucketListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the item information sent from ListActivity
        val itemId = intent.getIntExtra("item_id", -1)

        setContent {
            Uva_bucket_listTheme {

                // 2. Safely observe the state flow from the viewmode
                val bucketItems by viewModel.uiState.collectAsState()
                val bucketItem = bucketItems.find { it.id == itemId}

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // find() returns null if no item has this id, so only show the details when it exists
                    if (bucketItem != null) {
                        // 3. Pass the list and the callback method signature
                        DetailActivity(
                            modifier = Modifier.padding(innerPadding),
                            bucketItem,
                            onItemCheckedChange = { item, isChecked ->
                                viewModel.updateItemCompletion(item, isChecked)
                            },)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailActivity(
    modifier: Modifier,
    item: BucketItem,
    onItemCheckedChange: (BucketItem, Boolean) -> Unit) {
    // The Scaffold padding goes on the outer Column, and its contents are grouped in the middle of the screen
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = item.item_name,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = item.due_date,
                modifier = Modifier.padding(8.dp)
            )

            Checkbox(
                checked = item.completed,
                onCheckedChange = { isChecked ->
                    onItemCheckedChange(item, isChecked)
                }
            )

            if (item.completed_date != null) {
                Text(
                    text = item.completed_date,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {

                }
            ) {
                Text("Save")
            }

            val context = LocalContext.current
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text("Cancel")
            }

        }
    }
}
