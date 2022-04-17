package com.gomezdevlopment.expensetracker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Settings: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val homeArrow: ImageView = findViewById(R.id.homeArrow)

        homeArrow.setOnClickListener {
            onBackPressed()
        }
    }
}