package com.gomezdevlopment.expensetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext

class Settings: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val homeArrow: ImageView = findViewById(R.id.homeArrow)

        homeArrow.setOnClickListener {
            onBackPressed()
        }

        val currencies = resources.getStringArray(R.array.currencies)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, currencies)
        val dropdown: AutoCompleteTextView = findViewById(R.id.dropdownField)
        dropdown.setAdapter(arrayAdapter)

    }
}