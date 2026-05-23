package com.example.inventoryacc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val note: String? = null,
    val purchaseDate: Date,
    val createdAt: Long = System.currentTimeMillis()
)