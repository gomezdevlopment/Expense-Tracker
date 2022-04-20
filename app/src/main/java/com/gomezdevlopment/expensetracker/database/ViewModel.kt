package com.gomezdevlopment.expensetracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel (application: Application): AndroidViewModel(application) {

    var userEntries: LiveData<List<UserEntry>>
    var incomeEntries: LiveData<List<UserEntry>>
    var expenseEntries: LiveData<List<UserEntry>>
    private val repository: Repository

    init {
        val entryDao = UserEntryDatabase.getDatabase(application).entryDao()
        repository = Repository(entryDao)
        userEntries = repository.userEntries
        incomeEntries = repository.incomeEntries
        expenseEntries = repository.expenseEntries
    }

    fun addEntry(entry: UserEntry){
        viewModelScope.launch(Dispatchers.IO){
            repository.addEntry(entry)
        }
    }

    fun deleteEntry(entry: UserEntry){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteEntry(entry)
        }
    }
}