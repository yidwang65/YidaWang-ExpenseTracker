package com.bignerdranch.android.Expenseintent

import android.app.Application

class ExpenseIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ExpenseRepository.initialize(this)
    }
}
