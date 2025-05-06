package com.arian.foxalert.presentation.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.ui.theme.FoxColor40
import com.arian.foxalert.ui.theme.FoxColor80
import com.arian.foxalert.ui.theme.FoxGrey80

@Composable
fun CategoryScreen(
    categories: List<CategoryEntity>?,
    onDeleteClick: (category: CategoryEntity) -> Unit,
    onAddCategoryClick: (category: CategoryEntity) -> Unit,
) {
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (categories.isNullOrEmpty()) {
            Text("Category Not Found")
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        )
                ) {
                    IconButton(
                        onClick = {
                            showAddCategoryDialog = true
                        },
                        modifier = Modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = FoxColor40
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(category = category) {
                            onDeleteClick(category)
                        }
                    }
                }
            }
            if (showAddCategoryDialog) {
                AddCategoryDialog(
                    onDismissRequest = { showAddCategoryDialog = false },
                    onCategoryAdded = { newCategory ->
                        onAddCategoryClick(newCategory)
                        showAddCategoryDialog = false // Close dialog after adding
                    }
                )
            }

        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryEntity,
    onDeleteClick: (category: CategoryEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                FoxGrey80
            } else {
                Color.White
            }
        ),
        border = BorderStroke(1.dp, Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                IconButton(
                    onClick = { onDeleteClick(category) },
                    modifier = Modifier
                        .size(42.dp)  // Adjust size as needed
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,  // Delete icon from Material Design Icons
                        contentDescription = "Delete",
                        tint = FoxColor40
                    )
                }
            }

        }
    }
}


@Composable
fun AddCategoryDialog(
    onDismissRequest: () -> Unit,
    onCategoryAdded: (CategoryEntity) -> Unit
) {

    var categoryName by remember { mutableStateOf("") }
    var categoryColor by remember { mutableStateOf("#CCCCCC") } // Default color

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add New Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        val newCategory = CategoryEntity(
                            name = categoryName,
                            color = categoryColor
                        )
                        onCategoryAdded(newCategory)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)
            ) {
                Text("Add", color  = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest,  colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}
