package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.gomezdevlopment.expensetracker.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MainActivity : AppCompatActivity() {
    private val expensesList: ArrayList<Entry> = arrayListOf()
    private val incomeList: ArrayList<Entry> = arrayListOf()
    private val expensesAdapter = EntryAdapter(expensesList)
    private val incomeAdapter = EntryAdapter(incomeList)
    private var expensesTotal = 0f
    private var incomeTotal = 0f
    private var netTotal = 0f
    private var userName = "User"

    private lateinit var binding: ActivityMainBinding

    companion object{
        var currency = "$"
    }

    override fun onResume() {
        super.onResume()
        setTotals()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTotals()
        setTheme()

        binding.month.text = getMonth()


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, Stats::class.java)
            intent.putExtra("income", incomeTotal)
            intent.putExtra("expenses", expensesTotal)
            startActivity(intent)
        }

        binding.profileIcon.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }


        binding.addExpenseEntry.setOnClickListener {
            createDialog(this, true)
        }


        binding.addIncomeEntry.setOnClickListener {
            createDialog(this, false)
        }


        binding.expensesRecycler.layoutManager = LinearLayoutManager(this)
        binding.expensesRecycler.adapter = expensesAdapter


        binding.incomeRecycler.layoutManager = LinearLayoutManager(this)
        binding.incomeRecycler.adapter = incomeAdapter
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

        binding.expenses.text = expensesText
        binding.income.text = incomeText
        binding.net.text = netText

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
        val greeting = "Hello ${preferences.getString("username", "user")},"
        binding.userName.text = greeting
        if(preferences.getBoolean("initial_launch", true)){
            initialLaunch(this)
        }
        currency = preferences?.getString("currency", "$").toString()
        when (preferences?.getInt("theme", 2)) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> println("Nothing")
        }
    }

    private fun initialLaunch(context: Context){
        val dialog = Dialog(context, R.style.AlertDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.initial_launch_dialog)
        dialog.show()

        val name: EditText = dialog.findViewById(R.id.nameEditText)
        val submitButton: Button = dialog.findViewById(R.id.submitButton)

        fun isLetters(string: String): Boolean {
            return string.all { it.isLetter() }
        }

        submitButton.setOnClickListener {
            if(name.text.isNotEmpty()){
                if(isLetters(name.text.toString())){
                    userName = name.text.toString()
                    val greeting = "Hello $userName,"
                    binding.userName.text = greeting
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putBoolean("initial_launch", false).apply()
                    preferences.edit().putString("username", userName).apply()
                    dialog.dismiss()
                }else{
                    Toast.makeText(context, "Please enter alphabetic characters only. No spaces.", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, "Please enter a name, it can be a nickname or any username you prefer.", Toast.LENGTH_LONG).show()
            }

            setTotals()
        }
    }
}