package com.example.lr4_second.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "expenses")
data class ExpenseItem (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "expenseName")
    var expenseName: String,
    @ColumnInfo(name = "expenseValue")
    var expenseValue: String,
    @ColumnInfo(name = "imageUri")
    var imageUri: String? = null
)