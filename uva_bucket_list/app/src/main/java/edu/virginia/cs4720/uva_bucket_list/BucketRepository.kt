package edu.virginia.cs4720.uva_bucket_list

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

// BucketRepository.kt
object BucketRepository {

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
        BucketItem(11, "Finish homework", "2026-09-27", false, null),
        BucketItem(12, "Go grocery shopping", "2026-09-27", true, "2026-09-26"),
        BucketItem(13, "Clean bedroom", "2026-09-28", false, null),
        BucketItem(14, "Study for exam", "2026-09-28", false, null),
        BucketItem(15, "Study for exam", "2026-09-28", false, null),
        BucketItem(16, "Go for a run", "2026-09-29", true, "2026-09-25"),
        BucketItem(17, "Read a book", "2026-09-29", false, null),
        BucketItem(18, "Finish project", "2026-09-30", false, null),
        BucketItem(19, "Do laundry", "2026-09-30", true, "2026-09-26"),
        BucketItem(20, "Call Mom", "2026-10-01", false, null),
    )

    private val _items = MutableStateFlow<List<BucketItem>>(bucketItems)
    val items: StateFlow<List<BucketItem>> = _items.asStateFlow()
    fun updateItemCompletion(targetItem: BucketItem, isChecked: Boolean) {
        _items.update { currentList ->
            currentList.map { item ->
                if (item.id == targetItem.id) {
                    item.copy(
                        completed = isChecked,
                        // LocalDate.toString() is already yyyy-MM-dd
                        completed_date = if (isChecked) LocalDate.now().toString() else null
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
        _items.update { currentList ->

            val newItem = BucketItem(
                // One more than the highest id, so ids stay unique
                id = currentList.size + 1,
                item_name = itemName,
                due_date = dueDate,
                completed = false,
                completed_date = null
            )

            currentList + newItem
        }
    }
}
