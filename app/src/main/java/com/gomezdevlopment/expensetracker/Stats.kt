package com.gomezdevlopment.expensetracker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.gomezdevlopment.expensetracker.database.UserEntry
import com.gomezdevlopment.expensetracker.database.ViewModel
import com.gomezdevlopment.expensetracker.databinding.StatsBinding

class Stats: AppCompatActivity() {

    private lateinit var binding: StatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val income: Float = intent.extras?.get("income") as Float
        val expenses = intent.extras?.get("expenses") as Float

        val overviewValues = ArrayList<PieEntry>()
        overviewValues.add(PieEntry(expenses, "Expenses"))
        overviewValues.add(PieEntry(income, "Income"))
        setPieChart(overviewValues, binding.pieChart)

        val detailedValues: ArrayList<BarEntry> = arrayListOf()
        val userViewModel = ViewModelProvider(this)[ViewModel::class.java]
        val xAxisLabels: ArrayList<String> = arrayListOf()

        userViewModel.userEntries.observe(this) { entries ->
            var pos = 0f
            val sortedEntries = entries.sortedBy { it.value }
            for(entry in sortedEntries) {
                detailedValues.add(BarEntry(pos++, entry.value))
                xAxisLabels.add(entry.title)
            }
            setBarChart(detailedValues, binding.barChartDetailed, xAxisLabels)
        }

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

    private fun setBarChart(values: ArrayList<BarEntry>, chart: HorizontalBarChart, labels: ArrayList<String>){

        val barDataSet = BarDataSet(values, "Expenses and Income")

        barDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        chart.axisLeft.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.labelCount = labels.size
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.gridColor = ContextCompat.getColor(this, R.color.chartText)
        chart.xAxis.textColor = ContextCompat.getColor(this, R.color.chartText)


        val data = BarData(barDataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor(this, R.color.chartText))

        chart.legend.isEnabled = false
        chart.data = data
        chart.description = null
        chart.highlightValues(null)
        chart.invalidate()
        chart.animateXY(500, 500)
    }

}