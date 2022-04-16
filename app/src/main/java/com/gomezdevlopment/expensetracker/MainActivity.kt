package com.gomezdevlopment.expensetracker

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addExpenseEntry: Button = findViewById(R.id.button)
        addExpenseEntry.setOnClickListener {
            createDialog(this)
        }

        val addIncomeEntry: Button = findViewById(R.id.button2)
        addIncomeEntry.setOnClickListener {
            createDialog(this)
        }
    }
}

fun createDialog(context: Context){
    val dialog = Dialog(context, R.style.AlertDialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setContentView(R.layout.entry_creation_dialog)
    dialog.show()

    val submitButton: Button = dialog.findViewById(R.id.submitEntryButton)

    submitButton.setOnClickListener {
        dialog.dismiss()
    }
}