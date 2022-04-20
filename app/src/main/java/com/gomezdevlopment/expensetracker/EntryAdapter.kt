package com.gomezdevlopment.expensetracker

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency
import com.gomezdevlopment.expensetracker.database.UserEntry
import com.gomezdevlopment.expensetracker.database.ViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class EntryAdapter(private var context: Context): RecyclerView.Adapter<EntryAdapter.MyViewHolder>() {

    private var entryList = emptyList<UserEntry>()
    //val entryList: ArrayList<Entry> = entryList

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.label)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val entryCard: CardView = itemView.findViewById(R.id.entryCard)
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

        holder.entryCard.setOnClickListener {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

            builder.setTitle("Delete Entry")
            builder.setMessage("Are you sure you want to delete this entry?")

            builder.setPositiveButton("Yes") { dialog, which ->
                deleteEntryFromDatabase(currentEntry)
                Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(context, "Deletion Cancelled", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }
    }

    fun deleteEntryFromDatabase(entry: UserEntry){
        MainActivity.userViewModel.deleteEntry(entry)
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