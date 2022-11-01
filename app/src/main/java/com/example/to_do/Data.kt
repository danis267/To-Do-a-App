package com.example.to_do

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
class Data(
    @ColumnInfo(name = "task_title") val title: String?,
    @ColumnInfo(name = "task_details") val details: String?,
    @ColumnInfo(name = "task_completedDate") val completeTaskDate: String?,
    @ColumnInfo(name = "task_createdDate") val createTask: String?
) {
    @PrimaryKey(autoGenerate = true) var id = 0

}