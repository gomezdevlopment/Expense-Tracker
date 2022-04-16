package com.gomezdevlopment.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        val amount = entryList[position].amount
        val date = entryList[position].date

        holder.label.text = label
        holder.amount.text = amount.toString()
        holder.date.text = date
    }
}