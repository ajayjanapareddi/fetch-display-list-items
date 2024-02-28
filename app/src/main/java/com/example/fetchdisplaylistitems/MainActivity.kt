package com.example.fetchdisplaylistitems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.fetchdisplaylistitems.model.Item
import com.example.fetchdisplaylistitems.repository.ItemRepository
import com.example.fetchdisplaylistitems.viewmodel.GroupedItem
import com.example.fetchdisplaylistitems.viewmodel.ItemViewModel
import com.example.fetchdisplaylistitems.viewmodel.ItemViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ItemRepository()

        val viewModelFactory = ItemViewModelFactory(repository)

        val itemViewModel = ViewModelProvider(this, viewModelFactory)[ItemViewModel::class.java]

        setContent {
//            TestScreen()
            AppContent(itemViewModel)
        }
    }
}

@Composable
fun AppContent(viewModel: ItemViewModel) {
    ItemScreen(viewModel)
}

@Composable
fun ItemScreen(viewModel: ItemViewModel) {
    val items by viewModel.items.observeAsState(initial = emptyList())
    var selectedListId by remember { mutableStateOf(-1) }
    val lazyListState = rememberLazyListState()


    Column {
        ListIdFilter(items, onListIdSelected = { listId -> selectedListId = listId })
        ListHeading(title = "List of Items")
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Black, thickness = 2.dp)
        Spacer(modifier = Modifier.height(8.dp))

        LaunchedEffect(selectedListId) {
            lazyListState.scrollToItem(0) // Scroll to the top
        }

        ItemList(
            groupedItems = items,
            selectedListId = selectedListId,
            lazyListState = lazyListState
        )
    }
}

@Composable
fun ItemList(groupedItems: List<GroupedItem>, selectedListId: Int, lazyListState: LazyListState) {
    LazyColumn(state = lazyListState) {
        if (selectedListId == -1) {
            // No filter applied, show all items
            groupedItems.forEachIndexed { index, group ->
                if (index > 0) {
                    item { Divider(color = Color.Black, thickness = 2.dp) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                item { ListIdHeading(listId = group.listId) }
                items(group.items) { item ->
                    ItemView(item)
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        } else {
            // Filter applied, show items from the selected list ID only
            groupedItems.filter { it.listId == selectedListId }.forEach { group ->
                item {
                    ListIdHeading(listId = group.listId)
                }
                items(group.items) { item ->
                    ItemView(item = item)
                }
            }
        }
    }
}

@Composable
fun ListHeading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.Blue
    )
}

@Composable
fun ListIdHeading(listId: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Text(
            text = "List ID: $listId",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ItemView(item: Item) {
    item.name?.let {
        Text(
            text = it,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ListIdFilter(items: List<GroupedItem>, onListIdSelected: (Int) -> Unit) {
    val listIds = items.map { it.listId }.distinct()
    var expanded by remember { mutableStateOf(false) }
    val defaultFilterText = "No Filter"
    var selectedOptionText by remember { mutableStateOf(defaultFilterText) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(selectedOptionText)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            listIds.forEach { listId ->
                DropdownMenuItem(onClick = {
                    selectedOptionText = "List ID: $listId"
                    expanded = false
                    onListIdSelected(listId)
                }, text = {
                    Text(
                        "List ID: $listId",
                        fontWeight = if (selectedOptionText == "List ID: $listId") FontWeight.Bold else FontWeight.Normal
                    )
                })
            }
            DropdownMenuItem(onClick = {
                selectedOptionText = defaultFilterText
                expanded = false
                onListIdSelected(-1)
            }, text = {
                Text(
                    defaultFilterText,
                    fontWeight = if (selectedOptionText == defaultFilterText) FontWeight.Bold else FontWeight.Normal
                )
            })
        }
    }
}


@Composable
fun TestScreen() {
    LazyColumn {
        items(50) { index ->
            Text(
                "Item $index",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
