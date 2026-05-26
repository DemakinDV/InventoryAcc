package com.example.inventoryacc.data.dao

import androidx.room.*
import com.example.inventoryacc.domain.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE login = :login AND password = :password")
    suspend fun login(login: String, password: String): Account?
    
    @Query("SELECT * FROM accounts WHERE login = :login")
    suspend fun findByLogin(login: String): Account?
    
    @Query("SELECT * FROM accounts WHERE isLoggedIn = 1")
    suspend fun getLoggedInAccount(): Account?
    
    @Insert
    suspend fun insertAccount(account: Account)
    
    @Update
    suspend fun updateAccount(account: Account)
    
    @Query("UPDATE accounts SET isLoggedIn = 0")
    suspend fun logoutAll()
    
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>
}