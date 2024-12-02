package com.example.lr4_second.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey


@Entity (tableName = "expenses")
data class ExpenseItem (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "expenseName")
    var expenseName: String,
    @ColumnInfo(name = "expenseValue")
    var expenseValue: String,
)

@Entity(tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseItem::class,
            parentColumns = ["id"],
            childColumns = ["expenseItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Photos (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "expenseItemId")
    var expenseItemId: Int,
    @ColumnInfo(name = "imageUri")
    var imageUri: String
)

data class ExpensePhoto(
    val expenseName: String,
    val id: Int,
    val imageUri: String
)