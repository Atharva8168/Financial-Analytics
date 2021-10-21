package com.example.android.financialanalytics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import android.widget.LinearLayout as LinearLayout1
import androidx.recyclerview.widget.LinearLayoutManager as LinearLayoutManager

class EmiStatiscticsActivity : AppCompatActivity() {

    private var yearCount: TextView?=null
    private var principleCount:TextView?=null
    private var interestCount:TextView?=null
    private var balanceCount:TextView?=null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emi_statisctics)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if (bundle!= null){
            val P = bundle!!.getString("P")
            val N= bundle!!.getString("N")
            val I=bundle!!.getString("I")

        }

        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.layoutManager=LinearLayoutManager(this@EmiStatiscticsActivity)
        val count = ArrayList<StatisticsData>()



        var P = bundle!!.getString("P")!!.toFloat()
        var N= bundle!!.getString("N")!!.toFloat()
        var I= bundle!!.getString("I")!!.toFloat()
        var Emi=bundle!!.getString("Emi")!!.toFloat()
        var i=1


        while (P>=1.0){
            var givenInterest:Float=(P*I)/(12*100)
            var givenInterestFinal:String="%,.2f".format(givenInterest)
            var givenPrincipal:Float=(Emi-givenInterest)
            var givenPrincipalFinal:String="%,.2f".format(givenPrincipal)
            P=P-givenPrincipal
            var PrincipalFinal:String="%,.2f".format(P)


            count.add(StatisticsData(i.toString(),givenPrincipalFinal.toString(), givenInterestFinal.toString(),PrincipalFinal.toString()))
            i++
        }
        //creating our adapter
        val adapter = StatisticsAdapter(count)
        //now adding the adapter to recyclerview

        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter

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
}
