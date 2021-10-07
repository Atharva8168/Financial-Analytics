package com.example.android.financialanalytics

import androidx.room.*


@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    fun getAll():List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)

    @Query("SELECT label from transactions ORDER BY id ASC" )
    fun getLabel():List<String>

    @Query("SELECT amount from transactions ORDER BY id ASC")
    fun getAmount():List<Double>

}
