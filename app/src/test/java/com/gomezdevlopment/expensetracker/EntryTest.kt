package com.gomezdevlopment.expensetracker

import org.junit.Test

class EntryTest{

    @Test
    fun `print date to check formatting`(){
        val entry = Entry("food", 20F)
        println("${entry.date} is the date")
    }
}