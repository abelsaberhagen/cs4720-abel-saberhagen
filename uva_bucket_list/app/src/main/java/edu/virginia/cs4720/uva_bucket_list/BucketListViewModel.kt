package edu.virginia.cs4720.uva_bucket_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BucketListViewModel : ViewModel() {

    private val repository = edu.virginia.cs4720.uva_bucket_list.BucketRepository
    private val _uiState = MutableStateFlow<List<BucketItem>>(repository.bucketItems)
    val uiState: StateFlow<List<BucketItem>> = _uiState.asStateFlow()

    val currentDate = LocalDate.now()

    // Format the date to a custom layout
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = currentDate.format(formatter)

    // 2. The event method called by MainActivity when a checkbox is toggled
    fun updateItemCompletion(targetItem: BucketItem, isChecked: Boolean) {
        _uiState.update { currentList ->
            currentList.map { item ->
                if (item.id == targetItem.id) {
                    item.copy(
                        completed = isChecked,
                        completed_date = if (isChecked) formattedDate else null
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
