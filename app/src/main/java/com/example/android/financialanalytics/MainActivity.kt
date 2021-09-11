package com.example.android.financialanalytics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.LogManager

class MainActivity : AppCompatActivity() {
    private lateinit var transaction : List<Transaction>
    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var db : AppDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        transaction = arrayListOf()

        transactionAdapter = TransactionAdapter(transaction)
        linearLayoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(
            this,
             AppDatabase::class.java,
            "transactions").build()



        val recyclerView: RecyclerView = findViewById(R.id.recycle_view)
        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }


        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }
    private fun fetchAll(){
        GlobalScope.launch {
            transaction = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transaction)
            }
        }
    }

    private fun updateDashboard(){
        val totalAmount = transaction.map  {it.amount}.sum()
        val budgetAmount = transaction.filter {it.amount>0}.map { it.amount}.sum()
        val expenseAmount = totalAmount - budgetAmount

        var totalBudgetAmount : TextView = findViewById(R.id.total_balance_amount)
        var cardBudgetAmount : TextView = findViewById(R.id.budget_amount)
        var cardExpenseAmount : TextView = findViewById(R.id.expense_amount)

        totalBudgetAmount.text = "₹%.2f".format(totalAmount)
        cardBudgetAmount.text = "₹%.2f".format(budgetAmount)
        cardExpenseAmount.text = "₹%.2f".format(expenseAmount)

    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}