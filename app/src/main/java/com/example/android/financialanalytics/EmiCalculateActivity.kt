package com.example.android.financialanalytics

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_emi_calculate.*
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_rd.*
import java.text.DecimalFormat
import java.util.*

class EmiCalculateActivity : AppCompatActivity() {

    private var Principleamount: EditText? = null
    private var Interest: EditText? = null
    private var Year: EditText? = null
    private var Calculator: Button? = null
    private var MonthlyPayment: TextView? = null
    private var YearlyPayment: TextView? = null
    private var payableInterest: TextView? = null
    private var TotalWithInterest: TextView? = null
    private var reset: Button? = null
    private var statstics: Button? = null
    private var toggle1: ToggleButton? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emi_calculate)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        Principleamount = findViewById<View>(R.id.principleamount) as EditText
        Interest = findViewById<View>(R.id.fdInterest) as EditText
        Year = findViewById<View>(R.id.year) as EditText
        Calculator = findViewById<View>(R.id.calculator) as Button
        reset = findViewById<View>(R.id.reset) as Button
        statstics = findViewById<View>(R.id.statstics) as Button
        MonthlyPayment = findViewById<View>(R.id.MonthlyPayment) as TextView
        YearlyPayment = findViewById<View>(R.id.YearlyPayment) as TextView
        TotalWithInterest = findViewById<View>(R.id.TotalWithInterest) as TextView
        payableInterest = findViewById<View>(R.id.PayableInterest) as TextView
        toggle1 = findViewById<View>(R.id.toggle) as ToggleButton

        addTextFormater()



        Calculator!!.setOnClickListener {

            if (currentFocus != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }

            if (Principleamount!!.length() == 0) {
                Principleamount!!.setError(getString(R.string.get_principal_amount))
            } else if (Year!!.length() == 0) {
                Year!!.setError("Enter Year")
            } else if (Interest!!.length() == 0) {
                Interest!!.setError("Enter Interest Amount")
            } else if (Year!!.length() == 0 && Interest!!.length() == 0) {
                Year!!.setError("Enter Year")
                Interest!!.setError("Enter Interest Amount")
            } else if (Principleamount!!.length() == 0 && Interest!!.length() == 0) {
                Principleamount!!.setError("Enter Principle Amount")
                Interest!!.setError("Enter Interest Amount")
            } else if (Principleamount!!.length() == 0 && Year!!.length() == 0) {
                Principleamount!!.setError("Enter Principle Amount")
                Year!!.setError("Enter Year")
            } else {

                var P = Principleamount!!.text.toString().replace(",", "").toDouble()
                var N = Year!!.text.toString().toDouble()
                var I = Interest!!.text.toString().toDouble()
                if (toggle1!!.isChecked()) {
                    N = N
                } else {
                    N = N * 12

                }

                emi(P, I, N)
            }


        }


        reset!!.setOnClickListener {
            Principleamount!!.setText("")
            Interest!!.setText("")
            Year!!.setText("")
            MonthlyPayment!!.text = "0.0".toString()
            YearlyPayment!!.text = "0.0".toString()
            TotalWithInterest!!.text = "0.0".toString()
            payableInterest!!.text = "0.0".toString()
        }


        statstics!!.setOnClickListener {

            if (Principleamount!!.length() == 0) {
                Principleamount!!.setError("Enter Principle Amount")
            } else if (Year!!.length() == 0) {
                toggle1!!.setError("Enter Year")
            } else if (Interest!!.length() == 0) {
                Interest!!.setError("Enter Interest Amount")
            } else if (Year!!.length() == 0 && Interest!!.length() == 0) {
                toggle1!!.setError("Enter Year")
                Interest!!.setError("Enter Interest Amount")
            } else if (Principleamount!!.length() == 0 && Interest!!.length() == 0) {
                Principleamount!!.setError("Enter Principle Amount")
                Interest!!.setError("Enter Interest Amount")
            } else if (Principleamount!!.length() == 0 && Year!!.length() == 0) {
                Principleamount!!.setError("Enter Principle Amount")
                toggle1!!.setError("Enter Year")
            } else {

                var P = Principleamount!!.text.toString().replace(",", "").toDouble()
                var N = Year!!.text.toString().toDouble()
                var I = Interest!!.text.toString().toDouble()

                if (toggle1!!.isChecked()) {
                    N = N
                } else {
                    N = N * 12
                }

                var Emi = emi(P, I, N)
                val bundle = Bundle()
                bundle.putString("P", P.toDouble().toString())
                bundle.putString("N", N.toDouble().toString())
                bundle.putString("I", I.toDouble().toString())
                bundle.putString("Emi", Emi.toString())
                val intent = Intent(this@EmiCalculateActivity, EmiStatiscticsActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }


        }

    }
    private fun addTextFormater() {

        Principleamount!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charaters: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                charaters: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(editable: Editable?) {
                Principleamount!!.removeTextChangedListener(this)

                try {
                    val s = Principleamount!!.text.toString().replace(",", "")
                    val value = s.toDouble()
                    Principleamount?.setText(doubleToStringNoDecimal(value))
                    Principleamount?.setSelection(Principleamount!!.text.toString().length)


                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                Principleamount!!.addTextChangedListener(this)
            }
        })

    }
    fun doubleToStringNoDecimal(d: Double): String {
        val formatter = String.format("%,.0f", d)
        Log.d("number", formatter)
        return formatter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun emi(P: Double, I: Double, N: Double): Float {
        MonthlyPayment = findViewById<View>(R.id.MonthlyPayment) as TextView
        YearlyPayment = findViewById<View>(R.id.YearlyPayment) as TextView
        TotalWithInterest = findViewById<View>(R.id.TotalWithInterest) as TextView
        payableInterest = findViewById<View>(R.id.PayableInterest) as TextView

        var Month = N
        val InterestValue = I / 12 / 100
        var CommonPart = Math.pow(1 + InterestValue, Month)
        var DivUp = (P * InterestValue * CommonPart)
        var DivDown = CommonPart - 1
        var emiCalculationPerMonth: Float = ((DivUp / DivDown).toFloat())
        var emiCalculationPerYear = emiCalculationPerMonth * 12
        var totalInterest = (emiCalculationPerMonth * Month) - P
        var totalPayment = totalInterest + P


        var emiCalculationPerMonth1: String = "%.2f".format(emiCalculationPerMonth)
        var emiCalculationPerYear1: String = "%.2f".format(emiCalculationPerYear)
        var totalInterest1: String = "%.2f".format(totalInterest)
        var totalPayment1: String = "%.2f".format(totalPayment)

        MonthlyPayment!!.text = emiCalculationPerMonth1.toString().toFloat().toString()
        YearlyPayment!!.text = emiCalculationPerYear1.toString().toFloat().toString()
        payableInterest!!.text = totalInterest1.toString().toFloat().toString()
        TotalWithInterest!!.text = totalPayment1.toString().toFloat().toString()
        return emiCalculationPerMonth
    }
}