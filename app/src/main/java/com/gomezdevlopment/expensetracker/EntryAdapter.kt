package com.gomezdevlopment.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class EntryAdapter (entryList: ArrayList<Entry>): RecyclerView.Adapter<EntryAdapter.MyViewHolder>() {

    val entryList: ArrayList<Entry> = entryList

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.label)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val entryItem = LayoutInflater.from(parent.context).inflate(R.layout.entry_item, parent, false)
        return MyViewHolder(entryItem)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val label = entryList[position].title

        var formattedAmount: String = DecimalFormat("#,###.##").format(entryList[position].amount)
        if(currency == "€"){
            val europeanDecimalFormat = DecimalFormatSymbols(Locale.getDefault())
            europeanDecimalFormat.decimalSeparator = ','
            europeanDecimalFormat.groupingSeparator = '.'
            val europeanDecimalFormatter = DecimalFormat("#,###.##", europeanDecimalFormat)
            formattedAmount = europeanDecimalFormatter.format(entryList[position].amount)
        }

        val amount = "$currency$formattedAmount"
        val date = entryList[position].date

        holder.label.text = label
        holder.amount.text = amount
        holder.date.text = date
    }
}