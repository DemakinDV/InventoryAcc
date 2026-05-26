package com.example.inventoryacc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryacc.data.local.InventoryDatabase
import com.example.inventoryacc.data.repository.ItemRepositoryImpl
import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.domain.usecase.ItemUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InventoryViewModel(
    database: InventoryDatabase,
    private val accountId: String
) : ViewModel() {
    
    private val repository = ItemRepositoryImpl(database.itemDao(), accountId)
    private val itemUseCases = ItemUseCases(repository)
    
    val allItems = itemUseCases.getAllItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    private val _itemMap = mutableMapOf<String, MutableStateFlow<InventoryItem?>>()
    
    fun getItemById(id: String): StateFlow<InventoryItem?> {
        if (!_itemMap.containsKey(id)) {
            val flow = MutableStateFlow<InventoryItem?>(null)
            _itemMap[id] = flow
            viewModelScope.launch {
                val item = itemUseCases.getItemById(id)
                flow.value = item
            }
        }
        return _itemMap[id]!!
    }
    
    fun addItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.addItem(item)
        }
    }
    
    fun updateItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.updateItem(item)
        }
    }
    
    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.deleteItem(item)
        }
    }
}