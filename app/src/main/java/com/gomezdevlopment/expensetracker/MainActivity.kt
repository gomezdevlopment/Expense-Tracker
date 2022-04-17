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
import java.util.*
import kotlin.collections.ArrayList

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

        val month: TextView = findViewById(R.id.month)
        month.text = getMonth()

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

    private fun getMonth(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: String = when (calendar.get(Calendar.MONTH)) {
            0 -> getString(R.string.jan)
            1 -> getString(R.string.feb)
            2 -> getString(R.string.mar)
            3 -> getString(R.string.apr)
            4 -> getString(R.string.may)
            5 -> getString(R.string.jun)
            6 -> getString(R.string.jul)
            7 -> getString(R.string.aug)
            8 -> getString(R.string.sep)
            9 -> getString(R.string.oct)
            10 -> getString(R.string.nov)
            11 -> getString(R.string.dec)
            else -> "ERROR"
        }
        return "$month $year"
    }

}