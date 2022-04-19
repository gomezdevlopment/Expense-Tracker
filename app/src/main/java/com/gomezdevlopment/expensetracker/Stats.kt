package com.gomezdevlopment.expensetracker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.gomezdevlopment.expensetracker.databinding.StatsBinding

class Stats: AppCompatActivity() {

    private lateinit var binding: StatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val overviewValues = ArrayList<PieEntry>()
        overviewValues.add(PieEntry(500f, "Expenses"))
        overviewValues.add(PieEntry(1200f, "Income"))
        setPieChart(overviewValues, binding.pieChart)

        val detailedValues = ArrayList<PieEntry>()
        detailedValues.add(PieEntry(100f, "food"))
        detailedValues.add(PieEntry(200f, "gas"))
        detailedValues.add(PieEntry(300f, "bills"))
        detailedValues.add(PieEntry(1200f, "Income"))
        setPieChart(detailedValues, binding.pieChartDetailed)

    }

    private fun setPieChart(values: ArrayList<PieEntry>, chart: PieChart){
        val pieDataSet = PieDataSet(values, "Expenses and Income")

        pieDataSet.setDrawIcons(false)
        pieDataSet.sliceSpace = 3f
        pieDataSet.iconsOffset = MPPointF(0f, 40f)
        pieDataSet.selectionShift = 5f
        pieDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(pieDataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.setHoleColor(ContextCompat.getColor(this, R.color.background))
        chart.legend.isEnabled = false
        chart.data = data
        chart.description = null
        chart.highlightValues(null)
        chart.invalidate()
        chart.animateXY(500, 500)
    }

}