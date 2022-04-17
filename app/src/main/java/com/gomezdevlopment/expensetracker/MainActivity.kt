package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private val expensesList: ArrayList<Entry> = arrayListOf(Entry("food", 2.99f), Entry("gas", 20f))
    private val incomeList: ArrayList<Entry> = arrayListOf()
    private val expensesAdapter = EntryAdapter(expensesList)
    private val incomeAdapter = EntryAdapter(incomeList)
    private var expensesTotal = 0f
    private var incomeTotal = 0f
    private var netTotal = 0f
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

        val profileIcon: CircleImageView = findViewById(R.id.profile_image)
        profileIcon.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        val addExpenseEntry: Button = findViewById(R.id.button)
        addExpenseEntry.setOnClickListener {
            createDialog(this, true)
        }

        val addIncomeEntry: Button = findViewById(R.id.button2)
        addIncomeEntry.setOnClickListener {
            createDialog(this, false)
        }

        val expensesRecycler: RecyclerView = findViewById(R.id.expensesRecycler)
        expensesRecycler.layoutManager = LinearLayoutManager(this)
        expensesRecycler.adapter = expensesAdapter

        val incomeRecycler: RecyclerView = findViewById(R.id.incomeRecycler)
        incomeRecycler.layoutManager = LinearLayoutManager(this)
        incomeRecycler.adapter = incomeAdapter
    }

    private fun createDialog(context: Context, expense: Boolean) {
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
            if(expense){
                expensesList.add(Entry(labelText, amountFloat))
                expensesAdapter.notifyItemInserted(expensesList.size)
            }else{
                incomeList.add(Entry(labelText, amountFloat))
                incomeAdapter.notifyItemInserted(incomeList.size)
            }
            dialog.dismiss()
            setTotals()
        }
    }

    private fun calculateTotal(entries: ArrayList<Entry>): Float{
        var total = 0f
        for(entry in entries){
            total += entry.amount
        }
        return total
    }

    private fun setTotals(){
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