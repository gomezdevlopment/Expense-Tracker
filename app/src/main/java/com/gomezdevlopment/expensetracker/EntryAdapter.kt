package com.gomezdevlopment.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency
import com.gomezdevlopment.expensetracker.database.UserEntry
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class EntryAdapter: RecyclerView.Adapter<EntryAdapter.MyViewHolder>() {

    private var entryList = emptyList<UserEntry>()
    private var currencyType = "$"
    //val entryList: ArrayList<Entry> = entryList

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
        val currentEntry = entryList[position]
        val label = currentEntry.title

        var formattedValue: String = DecimalFormat("#,###.##").format(currentEntry.value)
        if(currency == "€"){
            val europeanDecimalFormat = DecimalFormatSymbols(Locale.getDefault())
            europeanDecimalFormat.decimalSeparator = ','
            europeanDecimalFormat.groupingSeparator = '.'
            val europeanDecimalFormatter = DecimalFormat("#,###.##", europeanDecimalFormat)
            formattedValue = europeanDecimalFormatter.format(currentEntry.value)
        }

        val amount = "$currency$formattedValue"
        val date = currentEntry.date

        holder.label.text = label
        holder.amount.text = amount
        holder.date.text = date
    }

    fun setData(entry: List<UserEntry>){
        this.entryList = entry
        notifyDataSetChanged()
    }

    fun notifyCurrencyChange(){
        notifyItemRangeChanged(0, entryList.size)
    }

    fun getTotal(): Float{
        var total = 0f
        for(entry in entryList){
            total += entry.value
        }
        return total
    }
}