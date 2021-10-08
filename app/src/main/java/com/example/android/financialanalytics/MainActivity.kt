package com.example.android.financialanalytics

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.transcation_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList as ArrayList1

class MainActivity : AppCompatActivity() {
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions : List<Transaction>
    private lateinit var oldTransaction : List<Transaction>
    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var db : AppDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        transactions = arrayListOf()

        transactionAdapter = TransactionAdapter(transactions)
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

        //for the swipe to delete

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                    return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])

            }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)


        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }


        createIncomePieChart()
        createExpensePieChart()

    }

    private fun fetchAll(){
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }
        }
    }

    private fun undoDelete(){
        GlobalScope.launch {
            db.transactionDao().insertAll(deletedTransaction)

            transactions = oldTransaction

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun showSnackbar(){
        val view = findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view,"Transaction Deleted!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()


    }

    private fun updateDashboard(){
        val totalAmount = transactions.map  {it.amount}.sum()
        val budgetAmount = transactions.filter {it.amount>0}.map { it.amount}.sum()
        val expenseAmount = totalAmount - budgetAmount

        var totalBudgetAmount : TextView = findViewById(R.id.total_balance_amount)
        var cardBudgetAmount : TextView = findViewById(R.id.budget_amount)
        var cardExpenseAmount : TextView = findViewById(R.id.expense_amount)

        totalBudgetAmount.text = "₹%.2f".format(totalAmount)
        cardBudgetAmount.text = "₹%.2f".format(budgetAmount)
        cardExpenseAmount.text = "₹%.2f".format(expenseAmount)

        createIncomePieChart()
        createExpensePieChart()

    }

    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransaction = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)
            transactions = transactions.filter { it.id != transaction.id}

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
                showSnackbar()
            }

        }
    }

    private fun createIncomePieChart(){
        val xvalues = ArrayList<String>()
        var bbbb  = transactions.filter {it.amount>0}.map { it.label}
        for (i in bbbb.indices) {
            xvalues.add(bbbb[i])
        }

        val yvalues = ArrayList1<Double>()
        val aaaa = transactions.filter { it.amount>0 }.map { it.amount }
        for (i in aaaa.indices){
            yvalues.add(Math.abs(aaaa[i]))
        }

        val piechartentry = ArrayList1<Entry>()
        for ((i,item) in  yvalues.withIndex()){
            piechartentry.add(Entry(item.toFloat(),i))
        }

        val piedataset = PieDataSet(piechartentry, "Income")
        piedataset.color = resources.getColor(R.color.green)
        incomePieChart.setDescription("  ")
        piedataset.sliceSpace = 2f
        val data = PieData(xvalues, piedataset)
        incomePieChart.data = data

    }

    private fun createExpensePieChart(){
        val xvalues = ArrayList<String>()
        var bbbb  = transactions.filter {it.amount<0}.map { it.label}
        for (i in bbbb.indices) {
            xvalues.add(bbbb[i])
        }

        val yvalues = ArrayList1<Double>()
        val aaaa = transactions.filter { it.amount<0 }.map { it.amount }
        for (i in aaaa.indices){
            yvalues.add(Math.abs(aaaa[i]))
        }

        val piechartentry = ArrayList1<Entry>()
        for ((i,item) in  yvalues.withIndex()){
            piechartentry.add(Entry(item.toFloat(),i))
        }

        val piedataset = PieDataSet(piechartentry, "Expense")
        piedataset.color = resources.getColor(R.color.red)
        expensePieChart.setDescription(" ")
        piedataset.sliceSpace = 2f
        val data = PieData(xvalues, piedataset)
        expensePieChart.data = data
    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}