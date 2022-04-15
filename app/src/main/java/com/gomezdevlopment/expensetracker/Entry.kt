package com.gomezdevlopment.expensetracker

import java.text.SimpleDateFormat
import java.util.*

class Entry (val title: String, val amount: Float){
    val date = formatDate()
}

fun formatDate(): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}