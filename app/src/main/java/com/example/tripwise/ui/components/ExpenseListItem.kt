package com.example.tripwise.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.example.tripwise.data.Expense
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseListItem(
    expense: Expense,
    onEditClick: () -> Unit,
//    navigateToDetail: (Long) -> Unit,
//    toggleSelection: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { /* navigateToDetail(trip.id) */ },
                onLongClick = { /* toggleSelection(trip.id) */ }
            )
            .clip(CardDefaults.shape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =  expense.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = expense.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Text(
                text = expense.cost.toString(),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = expense.currency.toString(),
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = expense.category,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}