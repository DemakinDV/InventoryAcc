package com.example.inventoryacc.network

import retrofit2.http.*

data class Account(
    val id: String,
    val login: String,
    val password: String,
    val isLoggedIn: Int,
    val createdAt: Long
)

data class InventoryItem(
    val id: String,
    val name: String,
    val quantity: Int?,
    val description: String?,
    val price: Double?,
    val purchaseDate: Long?,
    val saleDate: Long?,
    val note: String?,
    val isInStock: Int,
    val accountId: String,
    val createdAt: Long
)

interface ApiService {
    @GET("accounts")
    suspend fun getAccounts(): List<Account>

    @GET("items")
    suspend fun getItems(): List<InventoryItem>

    @GET("items/{accountId}")
    suspend fun getItemsByAccount(@Path("accountId") accountId: String): List<InventoryItem>

    @POST("items")
    suspend fun createItem(@Body item: InventoryItem): Map<String, String>

    @PUT("items/{itemId}")
    suspend fun updateItem(@Path("itemId") itemId: String, @Body item: InventoryItem): Map<String, String>

    @DELETE("items/{itemId}")
    suspend fun deleteItem(@Path("itemId") itemId: String): Map<String, String>

    @POST("login")
    suspend fun login(@Query("login") login: String, @Query("password") password: String): Account

    @POST("register")
    suspend fun register(@Body account: Account): Map<String, String>
}