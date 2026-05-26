package com.example.inventoryacc.data.dao

import androidx.room.*
import com.example.inventoryacc.domain.model.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM inventory_items WHERE accountId = :accountId ORDER BY createdAt DESC")
    fun getAllItems(accountId: String): Flow<List<InventoryItem>>

    @Query("SELECT * FROM inventory_items WHERE id = :id AND accountId = :accountId")
    suspend fun getItemById(id: String, accountId: String): InventoryItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryItem)

    @Update
    suspend fun updateItem(item: InventoryItem)

    @Delete
    suspend fun deleteItem(item: InventoryItem)

    @Query("DELETE FROM inventory_items WHERE accountId = :accountId")
    suspend fun deleteAllItems(accountId: String)
}