package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency
import com.gomezdevlopment.expensetracker.databinding.SettingsBinding

class Settings : AppCompatActivity() {

    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeArrow.setOnClickListener {
            onBackPressed()
        }

        binding.changeNameButton.setOnClickListener {
            changeUsername(this)
        }

        val preferences = getSharedPreferences("preferences", MODE_PRIVATE)

        val currencies = resources.getStringArray(R.array.currencies)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, currencies)
        binding.currencyDropdownField.setAdapter(arrayAdapter)

        binding.currencyDropdownField.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> currency = "$"
                1 -> currency = "£"
                2 -> currency = "€"
                3 -> currency = "¥"
                else -> println("Nothing")
            }
            preferences.edit().putString("currency", currency).apply()
        }

        val themes = arrayListOf("Light", "Dark", "System")
        val themeArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, themes)
        binding.themeDropdownField.setAdapter(themeArrayAdapter)

        binding.themeDropdownField.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    preferences.edit().putInt("theme", 0).apply()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    preferences.edit().putInt("theme", 1).apply()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.edit().putInt("theme", 2).apply()
                }
                else -> println("Nothing")
            }
            finish()
        }
    }

    private fun changeUsername(context: Context){
        val dialog = Dialog(context, R.style.AlertDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.change_username_dialog)
        dialog.show()

        val name: EditText = dialog.findViewById(R.id.nameEditText)
        val submitButton: Button = dialog.findViewById(R.id.submitButton)
        val cancelButton: Button = dialog.findViewById(R.id.cancelButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        fun isLetters(string: String): Boolean {
            return string.all { it.isLetter() }
        }

        fun checkUserName(name: String){
            if(name.isNotEmpty()){
                if(isLetters(name)){
                    val userName = name
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putString("username", userName).apply()
                    dialog.dismiss()
                    Toast.makeText(context, "Username Changed Successfully", Toast.LENGTH_LONG).show()
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