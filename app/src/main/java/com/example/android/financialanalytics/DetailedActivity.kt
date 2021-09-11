package com.example.android.financialanalytics

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.amount_edit_text
import kotlinx.android.synthetic.main.activity_add_transaction.amount_layout
import kotlinx.android.synthetic.main.activity_add_transaction.closeBtn
import kotlinx.android.synthetic.main.activity_add_transaction.description_edit_text
import kotlinx.android.synthetic.main.activity_add_transaction.label_edit_text
import kotlinx.android.synthetic.main.activity_add_transaction.label_layout
import kotlinx.android.synthetic.main.activity_detailed.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction : Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        label_edit_text.setText(transaction.label)
        amount_edit_text.setText(transaction.amount.toString())
        description_edit_text.setText(transaction.description)


        label_edit_text.addTextChangedListener {
            update_btn.visibility= View.VISIBLE
            if (it!!.count()>0)
                label_layout.error = null
        }

        amount_edit_text.addTextChangedListener {
            update_btn.visibility = View.VISIBLE
            if (it!!.count()>0)
                amount_layout.error = null
        }

        description_edit_text.addTextChangedListener {
            update_btn.visibility = View.VISIBLE
        }

        rootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }




        update_btn.setOnClickListener {
            val label = label_edit_text.text.toString()
            val amount = amount_edit_text.text.toString().toDoubleOrNull()
            val description : String = description_edit_text.text.toString()

            if (label.isEmpty())
                label_layout.error = "Please enter valid label"

            else if (amount == null)
                amount_layout.error = "Please enter valid amount"

            else{
                val transaction = Transaction(transaction.id ,label, amount,description)
                update(transaction)

            }

        }

        closeBtn.setOnClickListener{
            finish()
        }

    }

    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}