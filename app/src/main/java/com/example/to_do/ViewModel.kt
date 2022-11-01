package com.example.to_do

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application): AndroidViewModel(application) {
    private val repository: Repository
    val getAllData: LiveData<List<Data>>

    init {
        val database = TaskDatabase.getDatabase(application)
        val dao = database.dataDao()
        repository = Repository(dao)
        getAllData = repository.getAllData()
    }

    fun insert(data: Data) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(data)
        }
    }

    fun delete(data: Data) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(data)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun getAll(): LiveData<List<Data>> {
        return getAllData
    }


}