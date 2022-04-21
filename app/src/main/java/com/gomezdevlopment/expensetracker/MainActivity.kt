package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gomezdevlopment.expensetracker.database.UserEntry
import com.gomezdevlopment.expensetracker.database.ViewModel
import com.gomezdevlopment.expensetracker.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: EntryAdapter
    private lateinit var incomeAdapter: EntryAdapter
    private var expensesTotal = 0f
    private var incomeTotal = 0f
    private var netTotal = 0f
    private var userName = "User"

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    companion object{
        var currency = "$"
        lateinit var userViewModel: ViewModel
    }

    override fun onResume() {
        super.onResume()
        setTotals()
        setGreeting()
        setProfileImage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize lateinit variables
        expenseAdapter = EntryAdapter(this)
        incomeAdapter = EntryAdapter(this)
        preferences = getSharedPreferences("preferences", MODE_PRIVATE)
        userViewModel = ViewModelProvider(this)[ViewModel::class.java]

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


        binding.expensesRecycler.adapter = expenseAdapter
        binding.expensesRecycler.layoutManager = LinearLayoutManager(this)
        userViewModel.expenseEntries.observe(this) { entry ->
            expenseAdapter.setData(entry)
            setTotals()
        }

        binding.incomeRecycler.adapter = incomeAdapter
        binding.incomeRecycler.layoutManager = LinearLayoutManager(this)
        userViewModel.incomeEntries.observe(this) { entry ->
            incomeAdapter.setData(entry)
            setTotals()
        }
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
            if(amount.text.isNotEmpty() && label.text.isNotEmpty()){
                val labelText: String = label.text.toString()
                val amountFloat: Float = amount.text.toString().toFloat()
                if(expense){
                    val entry = Entry(labelText, amountFloat)
                    addEntryToDatabase(entry.title, entry.amount, entry.date, "expense")
                }else{
                    val entry = Entry(labelText, amountFloat)
                    addEntryToDatabase(entry.title, entry.amount, entry.date, "income")
                }
                dialog.dismiss()
                setTotals()
            }else{
                Toast.makeText(context, "Fields cannot be left blank.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addEntryToDatabase(title: String, amount: Float, date: String, entryType: String){
        val entry = UserEntry(0, title, amount, date, entryType)
        userViewModel.addEntry(entry)
    }

    private fun setTotals(){
        incomeTotal = incomeAdapter.getTotal()
        expensesTotal = expenseAdapter.getTotal()
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

        incomeAdapter.notifyCurrencyChange()
        expenseAdapter.notifyCurrencyChange()
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
        setGreeting()
        setProfileImage()
        if(preferences.getBoolean("initial_launch", true)){
            initialLaunch(this)
        }
        currency = preferences.getString("currency", "$").toString()
        when (preferences.getInt("theme", 2)) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> println("Nothing")
        }
    }

    private fun setGreeting(){
        val greeting = "Hello ${preferences.getString("username", "user")},"
        binding.userName.text = greeting
    }

    private fun setProfileImage(){
        if(File(filesDir, "profileImage.png").exists()){
            val bitmap = BitmapFactory.decodeFile(File(filesDir, "profileImage.png").toString())
            binding.profileIcon.setImageBitmap(bitmap)
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

        fun checkUserName(name: String){
            if(name.isNotEmpty()){
                if(isLetters(name)){
                    userName = name
                    preferences.edit().putBoolean("initial_launch", false).apply()
                    preferences.edit().putString("username", userName).apply()
                    setGreeting()
                    dialog.dismiss()
                }else{
                    Toast.makeText(context, "Please enter alphabetic characters only. No spaces.", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, "Please enter a name, it can be a nickname or any username you prefer.", Toast.LENGTH_LONG).show()
            }
        }

        submitButton.setOnClickListener {
            val submittedName = name.text.toString()
            checkUserName(submittedName)
        }

    }
}