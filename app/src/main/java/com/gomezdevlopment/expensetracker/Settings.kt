package com.gomezdevlopment.expensetracker

import android.os.Bundle
import android.widget.ArrayAdapter
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
}