package com.example.inventoryacc.domain.usecase

import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class ItemUseCases(
    private val repository: ItemRepository
) {
    fun getAllItems(): Flow<List<InventoryItem>> = repository.getAllItems()
    
    suspend fun getItemById(id: String): InventoryItem? = repository.getItemById(id)
    
    suspend fun addItem(item: InventoryItem) = repository.insertItem(item)
    
    suspend fun updateItem(item: InventoryItem) = repository.updateItem(item)
    
    suspend fun deleteItem(item: InventoryItem) = repository.deleteItem(item)
}