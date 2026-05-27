package com.example.officeapp.screens.assignDriver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> AssignmentSection(
    title: String,
    items: List<T>,
    emptyText: String,
    canLoadMore: Boolean,
    isLoading: Boolean,
    enabled: Boolean = true,
    onLoadMore: () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadNextPage by remember(items.size, canLoadMore, isLoading, enabled) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            enabled &&
                    canLoadMore &&
                    !isLoading &&
                    totalItemsCount > 0 &&
                    lastVisibleItemIndex >= totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onLoadMore()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(text = title)

        if (items.isEmpty() && !isLoading) {
            Text(
                text = emptyText,
                modifier = Modifier.padding(top = 8.dp)
            )
            return
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }

            if (isLoading && items.isNotEmpty()) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}