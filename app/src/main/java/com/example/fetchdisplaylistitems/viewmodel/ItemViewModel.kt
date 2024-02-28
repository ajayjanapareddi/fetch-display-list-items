package com.example.fetchdisplaylistitems.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.fetchdisplaylistitems.model.Item
import com.example.fetchdisplaylistitems.repository.ItemRepository
import kotlinx.coroutines.Dispatchers

class ItemViewModel(private val repository: ItemRepository): ViewModel() {

    val items: LiveData<List<GroupedItem>> = liveData(Dispatchers.IO) {
        repository.getItems().collect { rawData ->
            val processedData = processData(rawData)
            emit(processedData)
        }
    }

//    val items = liveData {
//        repository.getItems().collect { rawData ->
//            val processedData = processData(rawData)
//            emit(processedData)
//        }
//    }

    private fun processData(items: List<Item>): List<GroupedItem> {
        return items
            .filter { !it.name.isNullOrBlank() } // Filter out items with null or blank names
            .groupBy { it.listId } // Group items by listId
            .map { (listId, items) ->
                val sortedItems = sortByName(items) // Sort items within each group by name

                GroupedItem(
                    listId = listId,
                    items = sortedItems)
            }
            .sortedBy { it.listId } // Sort groups by listId
    }

    private fun sortByName(items: List<Item>): List<Item> {
        return items.sortedBy { it.name }
    }

    private fun sortByNumericValueInName(items: List<Item>): List<Item> {
        return items.sortedWith(Comparator { a, b ->
            val numA = a.name?.substringAfterLast(" ")?.toIntOrNull() ?: 0
            val numB = b.name?.substringAfterLast(" ")?.toIntOrNull() ?: 0
            numA.compareTo(numB)
        })
    }
}

data class GroupedItem(
    val listId: Int,
    val items: List<Item>
)
