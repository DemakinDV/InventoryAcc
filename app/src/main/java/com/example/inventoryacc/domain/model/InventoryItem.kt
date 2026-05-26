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
    val quantity: Int? = null,
    val description: String? = null,
    val price: Double? = null,
    val purchaseDate: Date? = null,
    val saleDate: Date? = null,
    val note: String? = null,
    val isInStock: Boolean = true,
    val accountId: String,
    val createdAt: Long = System.currentTimeMillis()
)