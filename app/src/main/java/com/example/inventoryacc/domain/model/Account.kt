package com.example.inventoryacc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val login: String,
    val password: String,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)