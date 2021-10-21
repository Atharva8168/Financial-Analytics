package com.example.android.financialanalytics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatisticsAdapter(private val statistics: ArrayList<StatisticsData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_ROW = 0
    private val ITEM_ROW = 1

    override fun getItemViewType(position: Int): Int {

        /*if(position == 0){
            return HEADER_ROW
        }*/

        return ITEM_ROW
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == HEADER_ROW){

            val v = LayoutInflater.from(parent.context).inflate(R.layout.stat_recycler_title_row, parent, false)
            return StatTitleViewHolder(v)
        }

        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return StatItemViewHolder(v)
    }

    //this method is binding the data on the list

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == ITEM_ROW)
        {
            val holderStatItem = viewHolder as StatItemViewHolder

            holderStatItem.bindItems(statistics[position])
        }
    }


    override fun getItemCount(): Int {
        return statistics.size
    }

    //the class is hodling the list view
    class StatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(statistics: StatisticsData) {
            val textViewYear = itemView.findViewById(R.id.yearCount) as TextView
            val textViewPrincipal  = itemView.findViewById(R.id.principleCount) as TextView
            val textViewInterest  = itemView.findViewById(R.id.interestCount) as TextView
            val textViewBalance  = itemView.findViewById(R.id.balanceCount) as TextView

            textViewYear.text = statistics.year
            textViewPrincipal.text = statistics.principle
            textViewBalance.text=statistics.balance
            textViewInterest.text=statistics.interest
        }
    }

    class StatTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
