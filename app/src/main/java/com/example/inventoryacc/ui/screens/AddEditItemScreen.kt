package com.example.inventoryacc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.ui.viewmodels.InventoryViewModel
import com.example.inventoryacc.utils.DateUtils
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(
    itemId: String?,
    onNavigateBack: () -> Unit,
    viewModel: InventoryViewModel = viewModel()
) {
    val context = LocalContext.current

    val existingItem by viewModel.getItemById(itemId ?: "").collectAsState()

    val isEditMode = itemId != null && existingItem != null

    var name by remember(existingItem) { mutableStateOf(existingItem?.name ?: "") }
    var description by remember(existingItem) { mutableStateOf(existingItem?.description ?: "") }
    var note by remember(existingItem) { mutableStateOf(existingItem?.note ?: "") }
    var purchaseDate by remember(existingItem) {
        mutableStateOf(existingItem?.purchaseDate?.let { DateUtils.formatDate(it) } ?: "")
    }

    if (itemId != null && existingItem == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Редактировать запас" else "Добавить запас") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isNotBlank() && description.isNotBlank() && purchaseDate.isNotBlank()) {
                                val date = DateUtils.parseDate(purchaseDate)
                                if (date != null) {
                                    if (isEditMode && existingItem != null) {
                                        viewModel.updateItem(
                                            existingItem!!.copy(
                                                name = name,
                                                description = description,
                                                note = note.ifBlank { null },
                                                purchaseDate = date
                                            )
                                        )
                                        Toast.makeText(context, "Запас обновлен", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val newItem = InventoryItem(
                                            name = name,
                                            description = description,
                                            note = note.ifBlank { null },
                                            purchaseDate = date
                                        )
                                        viewModel.addItem(newItem)
                                        Toast.makeText(context, "Запас добавлен", Toast.LENGTH_SHORT).show()
                                    }
                                    onNavigateBack()
                                } else {
                                    Toast.makeText(context, "Неверный формат даты. Используйте ДД.ММ.ГГГГ", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Сохранить")
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Примечание (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = purchaseDate,
                onValueChange = { purchaseDate = it },
                label = { Text("Дата приобретения (ДД.ММ.ГГГГ) *") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Пример: 15.03.2024") },
                singleLine = true
            )

            Text(
                text = "* - обязательные поля",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}