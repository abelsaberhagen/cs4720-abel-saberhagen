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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import edu.virginia.cs4720.uva_bucket_list.ui.theme.Uva_bucket_listTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BucketListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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


@Composable
fun BucketList(
    bucketListItems: List<BucketItem>,
    onItemCheckedChange: (BucketItem, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(bucketListItems) { item ->
            Row (modifier = modifier){

                Text(
                    text = item.item_name,
                    modifier = modifier
                )

                Text(
                    text = item.due_date,
                    modifier = modifier
                )

                Checkbox(
                    checked = item.completed,
                    onCheckedChange = { isChecked ->
                        onItemCheckedChange(item, isChecked)
                    }
                )

                if (item.completed_date != null) {
                    Text(
                        text =item.completed_date,
                        modifier = modifier
                    )
                }

                val context = LocalContext.current
                Button(
                    onClick = {
                        val intent = Intent(context, CreateActivity::class.java)
                        context.startActivity(intent)

                    }
                ) {

                }
            }
        }
    }
    Column {
        val context = LocalContext.current
        Button(
            onClick = {
                val intent = Intent(context, CreateActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Add")
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