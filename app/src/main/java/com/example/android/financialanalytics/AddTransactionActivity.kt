package com.example.android.financialanalytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)



        label_edit_text.addTextChangedListener {
            if (it!!.count()>0)
                label_layout.error = null
        }

        amount_edit_text.addTextChangedListener {
            if (it!!.count()>0)
                amount_layout.error = null
        }



        add_transaction_btn.setOnClickListener {
            val label = label_edit_text.text.toString()
            val amount = amount_edit_text.text.toString().toDoubleOrNull()
            val description : String = description_edit_text.text.toString()

            if (label.isEmpty())
                label_layout.error = "Please enter valid label"

            else if (amount == null)
                amount_layout.error = "Please enter valid amount"

            else{
                val transaction = Transaction(0,label, amount,description)
                insert(transaction)

            }

        }

        closeBtn.setOnClickListener{
            finish()
        }

    }

    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}