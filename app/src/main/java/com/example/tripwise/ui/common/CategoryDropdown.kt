//package com.example.tripwise.ui.common
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun CategoryDropdown(
//    selectedCategory: String,
//    onCategorySelected: (String) -> Unit,
//    categories: List<String> = listOf("Accommodation", "Food & Dining", "Transportation", "Entertainment", "Shopping", "Add Custom Category…")
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier
//        .fillMaxWidth()
//        .padding(start = 50.dp, end = 50.dp)
//    ) {
//        OutlinedTextField(
//            value = selectedCategory,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Category") },
//            trailingIcon = {
//                Icon(
//                    Icons.Default.ArrowDropDown,
//                    contentDescription = "Dropdown Arrow",
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = !expanded }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            categories.forEach { category ->
//                DropdownMenuItem(
//                    onClick = {
//                        onCategorySelected(category)
//                        expanded = false
//                    },
//                    text = {
//                        Text(text = category)
//                    }
//                )
//            }
//        }
//    }
//}

package com.example.tripwise.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> CategoryDropdown(
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    items: List<T>,
    label: String = "Select an option",
    itemToString: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
    ) {
        OutlinedTextField(
            value = itemToString(selectedItem),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = {
                        Text(text = itemToString(item))
                    }
                )
            }
        }
    }
}
