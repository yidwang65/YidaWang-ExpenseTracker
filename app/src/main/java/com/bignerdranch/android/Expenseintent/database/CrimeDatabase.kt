package com.bignerdranch.android.Expenseintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.Expenseintent.Expense

@Database(entities = [Expense::class], version = 2, exportSchema = false)
@TypeConverters(ExpenseTypeConverters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Expense ADD COLUMN amount Double NOT NULL DEFAULT ''"
        )
    }
}
