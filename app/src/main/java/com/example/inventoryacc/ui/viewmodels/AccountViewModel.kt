package com.example.inventoryacc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryacc.data.local.InventoryDatabase
import com.example.inventoryacc.domain.model.Account
import kotlinx.coroutines.launch

class AccountViewModel(
    private val database: InventoryDatabase
) : ViewModel() {
    
    suspend fun getLoggedInAccount(): Account? {
        return database.accountDao().getLoggedInAccount()
    }
    
    fun login(login: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val account = database.accountDao().login(login, password)
                if (account != null) {
                    database.accountDao().logoutAll()
                    database.accountDao().updateAccount(account.copy(isLoggedIn = true))
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
    
    fun register(login: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val existing = database.accountDao().findByLogin(login)
                if (existing == null) {
                    val newAccount = Account(login = login, password = password, isLoggedIn = true)
                    database.accountDao().logoutAll()
                    database.accountDao().insertAccount(newAccount)
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
    
    fun logout(onResult: () -> Unit) {
        viewModelScope.launch {
            database.accountDao().logoutAll()
            onResult()
        }
    }
}