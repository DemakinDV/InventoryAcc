package com.example.inventoryacc.data.repository

import com.example.inventoryacc.data.dao.ItemDao
import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class ItemRepositoryImpl(
    private val itemDao: ItemDao,
    private val accountId: String
) : ItemRepository {
    override fun getAllItems(): Flow<List<InventoryItem>> = itemDao.getAllItems(accountId)
    
    override suspend fun getItemById(id: String): InventoryItem? = itemDao.getItemById(id, accountId)
    
    override suspend fun insertItem(item: InventoryItem) = itemDao.insertItem(item)
    
    override suspend fun updateItem(item: InventoryItem) = itemDao.updateItem(item)
    
    override suspend fun deleteItem(item: InventoryItem) = itemDao.deleteItem(item)
}