package com.bignerdranch.android.Expenseintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Expense(
    @PrimaryKey val id: UUID,
    val title: Int,
    val date: Date,
    val amount: Double
)
