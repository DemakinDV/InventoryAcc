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
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(
    itemId: String?,
    onNavigateBack: () -> Unit,
    viewModel: InventoryViewModel = viewModel(),
    accountId: String
) {
    val context = LocalContext.current
    val existingItemState by viewModel.getItemById(itemId ?: "").collectAsState()
    val existingItem = existingItemState

    val isEditMode = itemId != null && existingItem != null

    var name by remember(existingItem) { mutableStateOf(existingItem?.name ?: "") }
    var quantity by remember(existingItem) { mutableStateOf(existingItem?.quantity?.toString() ?: "") }
    var description by remember(existingItem) { mutableStateOf(existingItem?.description ?: "") }
    var price by remember(existingItem) { mutableStateOf(existingItem?.price?.toString() ?: "") }
    var purchaseDate by remember(existingItem) {
        mutableStateOf(existingItem?.purchaseDate?.let { DateUtils.formatDate(it) } ?: "")
    }
    var saleDate by remember(existingItem) {
        mutableStateOf(existingItem?.saleDate?.let { DateUtils.formatDate(it) } ?: "")
    }
    var note by remember(existingItem) { mutableStateOf(existingItem?.note ?: "") }
    var isInStock by remember(existingItem) { mutableStateOf(existingItem?.isInStock ?: true) }

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
                title = { Text(if (isEditMode) "Редактировать товар" else "Добавить товар") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                val purchaseDateObj = if (purchaseDate.isNotBlank()) DateUtils.parseDate(purchaseDate) else null
                                val saleDateObj = if (saleDate.isNotBlank()) DateUtils.parseDate(saleDate) else null
                                val quantityInt = quantity.toIntOrNull()
                                val priceDouble = price.toDoubleOrNull()

                                if (isEditMode && existingItem != null) {
                                    viewModel.updateItem(
                                        existingItem.copy(
                                            name = name,
                                            quantity = quantityInt,
                                            description = description.ifBlank { null },
                                            price = priceDouble,
                                            purchaseDate = purchaseDateObj,
                                            saleDate = saleDateObj,
                                            note = note.ifBlank { null },
                                            isInStock = isInStock
                                        )
                                    )
                                    Toast.makeText(context, "Товар обновлен", Toast.LENGTH_SHORT).show()
                                } else {
                                    val newItem = InventoryItem(
                                        name = name,
                                        quantity = quantityInt,
                                        description = description.ifBlank { null },
                                        price = priceDouble,
                                        purchaseDate = purchaseDateObj,
                                        saleDate = saleDateObj,
                                        note = note.ifBlank { null },
                                        isInStock = isInStock,
                                        accountId = accountId
                                    )
                                    viewModel.addItem(newItem)
                                    Toast.makeText(context, "Товар добавлен", Toast.LENGTH_SHORT).show()
                                }
                                onNavigateBack()
                            } else {
                                Toast.makeText(context, "Заполните название товара", Toast.LENGTH_SHORT).show()
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
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Количество") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Цена") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = purchaseDate,
                onValueChange = { purchaseDate = it },
                label = { Text("Дата покупки (ДД.ММ.ГГГГ)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Пример: 15.03.2024") },
                singleLine = true
            )

            OutlinedTextField(
                value = saleDate,
                onValueChange = { saleDate = it },
                label = { Text("Дата продажи (ДД.ММ.ГГГГ)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Пример: 20.03.2024") },
                singleLine = true
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Примечание") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Товар на складе")
                Switch(
                    checked = isInStock,
                    onCheckedChange = { isInStock = it }
                )
            }

            Text(
                text = "* - обязательные поля",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}