package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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

    companion object{
        var currency = "$"
    }

    override fun onResume() {
        super.onResume()
        setTotals()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenses = findViewById(R.id.expenses)
        income = findViewById(R.id.income)
        net = findViewById(R.id.net)
        setTotals()
        setTheme()


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

        val formattedExpenseTotal: String
        val formattedIncomeTotal: String
        val formattedNetTotal: String

        if(currency == "€"){
            val europeanDecimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
            europeanDecimalFormatSymbols.decimalSeparator = ','
            europeanDecimalFormatSymbols.groupingSeparator = '.'
            val europeanDecimalFormat = DecimalFormat("#,###.##", europeanDecimalFormatSymbols)
            formattedExpenseTotal = europeanDecimalFormat.format(expensesTotal)
            formattedIncomeTotal = europeanDecimalFormat.format(incomeTotal)
            formattedNetTotal = europeanDecimalFormat.format(netTotal)
        }else{
            formattedExpenseTotal = DecimalFormat("#,###.##").format(expensesTotal)
            formattedIncomeTotal = DecimalFormat("#,###.##").format(incomeTotal)
            formattedNetTotal = DecimalFormat("#,###.##").format(netTotal)
        }

        val expensesText = "-$currency$formattedExpenseTotal"
        val incomeText = "+$currency$formattedIncomeTotal"
        val netText = "$currency$formattedNetTotal"

        expenses.text = expensesText
        income.text = incomeText
        net.text = netText

        expensesAdapter.notifyItemRangeChanged(0, expensesList.size)
        incomeAdapter.notifyItemRangeChanged(0, incomeList.size)
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

    private fun setTheme(){
        val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
        when (preferences?.getInt("theme", 2)) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> println("Nothing")
        }
    }
}