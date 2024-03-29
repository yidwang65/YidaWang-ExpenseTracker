package com.bignerdranch.android.Expenseintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.Expenseintent.database.ExpenseDatabase
import com.bignerdranch.android.Expenseintent.database.migration_1_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "expense-database"

class ExpenseRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: ExpenseDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ExpenseDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()

    fun getExpenses(): Flow<List<Expense>> = database.expenseDao().getExpenses()

    suspend fun getExpense(id: UUID): Expense = database.expenseDao().getExpense(id)

    fun updateExpense(expense: Expense) {
        coroutineScope.launch {
            database.expenseDao().updateExpense(expense)
        }
    }

    suspend fun addExpense(expense: Expense) {
        database.expenseDao().addExpense(expense)
    }

    companion object {
        private var INSTANCE: ExpenseRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ExpenseRepository(context)
            }
        }

        fun get(): ExpenseRepository {
            return INSTANCE
                ?: throw IllegalStateException("ExpenseRepository must be initialized")
        }
    }
}
