package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val expensesList: ArrayList<Entry> = arrayListOf(Entry("food", 2.99f), Entry("gas", 20f))
    val incomeList: ArrayList<Entry> = arrayListOf()
    val expensesAdapter = EntryAdapter(expensesList)
    var expensesTotal = 0f
    var incomeTotal = 0f
    var netTotal = 0f
    private lateinit var expenses: TextView
    private lateinit var income: TextView
    private lateinit var net: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenses = findViewById(R.id.expenses)
        income = findViewById(R.id.income)
        net = findViewById(R.id.net)
        setTotals()


        val addExpenseEntry: Button = findViewById(R.id.button)
        addExpenseEntry.setOnClickListener {
            createDialog(this)
        }

        val addIncomeEntry: Button = findViewById(R.id.button2)
        addIncomeEntry.setOnClickListener {
            createDialog(this)
        }

        val expensesRecycler: RecyclerView = findViewById(R.id.expensesRecycler)
        expensesRecycler.layoutManager = LinearLayoutManager(this)
        expensesRecycler.adapter = expensesAdapter
    }

    fun createDialog(context: Context) {
        val dialog = Dialog(context, R.style.AlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.entry_creation_dialog)
        dialog.show()

        val label: EditText = dialog.findViewById(R.id.labelTextInputEditText)
        val amount: EditText = dialog.findViewById(R.id.amountTextInputEditText)
        val submitButton: Button = dialog.findViewById(R.id.submitEntryButton)

        submitButton.setOnClickListener {
            val labelText: String = label.text.toString()
            val amountFloat: Float = amount.text.toString().toFloat()
            expensesList.add(Entry(labelText, amountFloat))
            dialog.dismiss()
            expensesAdapter.notifyItemInserted(expensesList.size)
            setTotals()
        }
    }

    fun calculateTotal(entries: ArrayList<Entry>): Float{
        var total = 0f
        for(entry in entries){
            total += entry.amount
        }
        return total
    }

    fun setTotals(){
        expensesTotal = calculateTotal(expensesList)
        incomeTotal = calculateTotal(incomeList)
        netTotal = incomeTotal-expensesTotal

        val expensesText = "-\$${String.format("%.2f", expensesTotal)}"
        expenses.text = expensesText

        val incomeText = "+\$${String.format("%.2f", incomeTotal)}"
        income.text = incomeText

        val netText = "\$${String.format("%.2f", netTotal)}"
        net.text = netText
    }

}