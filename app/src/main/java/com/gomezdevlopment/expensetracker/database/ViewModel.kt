package com.gomezdevlopment.expensetracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel (application: Application): AndroidViewModel(application) {

    private var userEntries: LiveData<List<UserEntry>>
    private val repository: Repository

    init {
        val entryDao = UserEntryDatabase.getDatabase(application).entryDao()
        repository = Repository(entryDao)
        userEntries = repository.userEntries
    }

    fun addEntry(entry: UserEntry){
        viewModelScope.launch(Dispatchers.IO){
            repository.addEntry(entry)
        }
    }
}