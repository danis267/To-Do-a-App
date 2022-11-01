package com.example.to_do

import androidx.lifecycle.LiveData

class Repository(private val dao: DataDao) {

    val getAllData: LiveData<List<Data>> = dao.getAllData()

    suspend fun insert(data: Data) {
        dao.insert(data)
    }

    suspend fun delete(data: Data) {
        dao.delete(data)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

     fun getAllData(): LiveData<List<Data>> {
        return getAllData
    }
}

