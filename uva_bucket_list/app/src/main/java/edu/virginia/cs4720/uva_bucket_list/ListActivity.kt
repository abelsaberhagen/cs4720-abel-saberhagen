package edu.virginia.cs4720.uva_bucket_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.uva_bucket_list.ui.theme.Uva_bucket_listTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel: BucketListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Uva_bucket_listTheme {
                // 2. Safely observe the state flow from the viewmodel
                val bucketItems by viewModel.uiState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 3. Pass the list and the callback method signature
                    BucketList(
                        bucketListItems = bucketItems,
                        onItemCheckedChange = { item, isChecked ->
                            // This lambda delegates the event up to the viewmodel
                            viewModel.updateItemCompletion(item, isChecked)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun BucketList(
    bucketListItems: List<BucketItem>,
    onItemCheckedChange: (BucketItem, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // The Scaffold padding goes on the outer container once, not on every row
    Column(modifier = modifier.fillMaxSize()) {
        // weight(1f) gives the list all the space left over after the Add button,
        // so it has a fixed height to scroll within and the button sits below it
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(bucketListItems) { item ->
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

                    // Pen button opens the detail screen for this item
                    IconButton(
                        onClick = {
                            val intent = Intent(context, DetailActivity::class.java).apply {
                                putExtra("item_id", item.id)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "Edit ${item.item_name}",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                val intent = Intent(context, CreateActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Add Item")
        }
    }
}


@Preview
@Composable
fun BucketListPreview() {
    BucketList(
        bucketListItems = listOf(
            BucketItem(
                1,
                "Finish homework",
                "2026-09-27",
                false,
                null
            ),
            BucketItem(
                2,
                "Go grocery shopping",
                "2026-09-28",
                true,
                "2026-09-26"
            )
        ),
        onItemCheckedChange = { item, checked -> }
    )
}