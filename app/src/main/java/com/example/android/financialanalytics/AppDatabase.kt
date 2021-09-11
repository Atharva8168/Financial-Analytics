package com.example.android.financialanalytics

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = arrayOf(Transaction::class),version = 1 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao
}