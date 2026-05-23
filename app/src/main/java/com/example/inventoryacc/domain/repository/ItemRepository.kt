package com.example.inventoryacc.domain.repository

import com.example.inventoryacc.domain.model.InventoryItem
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<List<InventoryItem>>
    suspend fun getItemById(id: String): InventoryItem?
    suspend fun insertItem(item: InventoryItem)
    suspend fun updateItem(item: InventoryItem)
    suspend fun deleteItem(item: InventoryItem)
}