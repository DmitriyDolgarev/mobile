package com.example.lr4_second.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertItem(item: ExpenseItem)
    @Query("SELECT * FROM expenses")
    fun getAllItems(): Flow<List<ExpenseItem>>
    @Query("DELETE FROM expenses WHERE expenseName = :expenseName")
    fun deleteItemByName(expenseName: String)
    @Query("UPDATE expenses SET expenseName = :newName, expenseValue = :newValue WHERE expenseName = :expenseName")
    fun updateItem(expenseName: String, newName: String, newValue: String)
    @Query("UPDATE expenses SET imageUri = :uri WHERE expenseName = :expenseName")
    fun updateExpenseImage(expenseName: String, uri:String)
}