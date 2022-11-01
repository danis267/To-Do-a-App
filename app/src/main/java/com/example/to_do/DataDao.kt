package com.example.to_do

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DataDao {

    @Insert
    suspend fun insert(data: Data)

    @Delete
    suspend fun delete(data: Data)

    @Query("delete from task_table ")
    suspend fun deleteAll()

    @Query("Select * from task_table order by id DESC")
    fun getAllData(): LiveData<List<Data>>
}