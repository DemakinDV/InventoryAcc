package com.example.inventoryacc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.ui.viewmodels.InventoryViewModel
import com.example.inventoryacc.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: InventoryViewModel = viewModel()
) {
    val item by viewModel.getItemById(itemId).collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (item == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить товар") },
            text = { Text("Вы уверены, что хотите удалить \"${item?.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        item?.let { viewModel.deleteItem(it) }
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item!!.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(itemId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Название",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item!!.name,
                        fontSize = 20.sp
                    )
                    
                    HorizontalDivider()
                    
                    item!!.quantity?.let {
                        Text(
                            text = "Количество",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = it.toString(), fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    item!!.description?.let {
                        Text(
                            text = "Описание",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = it, fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    item!!.price?.let {
                        Text(
                            text = "Цена",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "$it ₽", fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    item!!.purchaseDate?.let {
                        Text(
                            text = "Дата покупки",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = DateUtils.formatDate(it), fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    item!!.saleDate?.let {
                        Text(
                            text = "Дата продажи",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = DateUtils.formatDate(it), fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    item!!.note?.let {
                        Text(
                            text = "Примечание",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = it, fontSize = 16.sp)
                        HorizontalDivider()
                    }
                    
                    Text(
                        text = "Статус",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (item!!.isInStock) "На складе" else "Продано",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}