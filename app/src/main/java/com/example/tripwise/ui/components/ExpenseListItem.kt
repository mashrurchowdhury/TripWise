package com.example.tripwise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.tripwise.data.Expense

@Composable
fun ExpenseListItem(
    expense: Expense,
    onEditClick: () -> Unit,
    showLocalCurrency: Boolean,
    homeCurrency: String,
    modifier: Modifier = Modifier,
) {
    val displayCost = if (!showLocalCurrency) {
        "${expense.cost} ${expense.currency}"
    } else {
        "${expense.convertedCost ?: expense.cost} $homeCurrency"
    }

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = expense.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = displayCost,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = expense.currency,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = expense.category,
                style = MaterialTheme.typography.labelMedium,
            )

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
