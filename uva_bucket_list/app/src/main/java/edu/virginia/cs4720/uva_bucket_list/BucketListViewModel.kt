package edu.virginia.cs4720.uva_bucket_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BucketListViewModel : ViewModel() {
    val bucketItems = listOf(
        BucketItem(1, "Finish homework", "2026-09-27", false, null),
        BucketItem(2, "Go grocery shopping", "2026-09-27", true, "2026-09-26"),
        BucketItem(3, "Clean bedroom", "2026-09-28", false, null),
        BucketItem(4, "Study for exam", "2026-09-28", false, null),
        BucketItem(5, "Go for a run", "2026-09-29", true, "2026-09-25"),
        BucketItem(6, "Read a book", "2026-09-29", false, null),
        BucketItem(7, "Finish project", "2026-09-30", false, null),
        BucketItem(8, "Do laundry", "2026-09-30", true, "2026-09-26"),
        BucketItem(9, "Call Mom", "2026-10-01", false, null),
        BucketItem(10, "Submit assignment", "2026-10-01", true, "2026-09-24"),
    )

    // 1. Holds the stateful list of your 100 items (using the mock list from earlier)
    private val _uiState = MutableStateFlow<List<BucketItem>>(bucketItems)
    val uiState: StateFlow<List<BucketItem>> = _uiState.asStateFlow()

    // 2. The event method called by MainActivity when a checkbox is toggled
    fun updateItemCompletion(targetItem: BucketItem, isChecked: Boolean) {
        _uiState.update { currentList ->
            currentList.map { item ->
                if (item.id == targetItem.id) {
                    item.copy(
                        completed = isChecked,
                        completed_date = if (isChecked) "2026-09-26" else null
                    )
                } else {
                    item
                }
            }
        }
    }

    fun addItem(
        itemName: String,
        dueDate: String
    ) {
        _uiState.update { currentList ->

            val newItem = BucketItem(
                id = (currentList.maxOfOrNull { it.id } ?: 0) + 1,
                item_name = itemName,
                due_date = dueDate,
                completed = false,
                completed_date = null
            )

            currentList + newItem
        }
    }
}
