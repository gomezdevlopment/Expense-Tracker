package com.gomezdevlopment.expensetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val homeArrow: ImageView = findViewById(R.id.homeArrow)

        homeArrow.setOnClickListener {
            onBackPressed()
        }

        val currencies = resources.getStringArray(R.array.currencies)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, currencies)
        val dropdown: AutoCompleteTextView = findViewById(R.id.currencyDropdownField)
        dropdown.setAdapter(arrayAdapter)

        dropdown.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> currency = "$"
                1 -> currency = "£"
                2 -> currency = "€"
                3 -> currency = "¥"
                else -> println("Nothing")
            }
        }

        val themes = arrayListOf("Light", "Dark", "System")
        val themeArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, themes)
        val themeDropdown: AutoCompleteTextView = findViewById(R.id.themeDropdownField)
        themeDropdown.setAdapter(themeArrayAdapter)

        themeDropdown.setOnItemClickListener { _, _, i, _ ->
            val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
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
}