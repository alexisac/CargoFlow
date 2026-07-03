package com.example.officeapp.screens.dashboard

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.officeapp.models.dashboard.DashboardChartItem
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun OfficeDashboardCharts(
    tripsByStatusItems: List<DashboardChartItem>,
    vehicleUtilizationItems: List<DashboardChartItem>,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TripsByStatusPieChart(
            items = tripsByStatusItems,
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            borderColor = borderColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        VehicleUtilizationHorizontalBarChart(
            items = vehicleUtilizationItems,
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            borderColor = borderColor
        )
    }
}

@Composable
private fun TripsByStatusPieChart(
    items: List<DashboardChartItem>,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color
) {
    DashboardChartCard(
        title = "Trips by status",
        subtitle = "Current operational distribution",
        cardColor = cardColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        borderColor = borderColor
    ) {
        if (items.isEmpty() || items.sumOf { it.value.toDouble() } == 0.0) {
            Text(
                text = "No trip data available yet.",
                color = secondaryTextColor,
                fontSize = 14.sp
            )
            return@DashboardChartCard
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false

                    setUsePercentValues(true)
                    setDrawEntryLabels(false)
                    setDrawHoleEnabled(true)

                    holeRadius = 56f
                    transparentCircleRadius = 60f
                    setTransparentCircleAlpha(0)
                    setHoleColor(AndroidColor.TRANSPARENT)

                    centerText = "Trips"
                    setCenterTextSize(16f)
                    setCenterTextColor(textColor.toArgb())

                    setBackgroundColor(AndroidColor.TRANSPARENT)
                    setNoDataTextColor(textColor.toArgb())

                    isRotationEnabled = false
                    setTouchEnabled(false)

                    data = createPieData(
                        chart = this,
                        items = items,
                        textColor = textColor
                    )

                    invalidate()
                }
            },
            update = { chart ->
                chart.centerText = "Trips"
                chart.setCenterTextColor(textColor.toArgb())
                chart.data = createPieData(
                    chart = chart,
                    items = items,
                    textColor = textColor
                )
                chart.invalidate()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ChartLegend(
            items = items,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor
        )
    }
}

private fun createPieData(
    chart: PieChart,
    items: List<DashboardChartItem>,
    textColor: Color
): PieData {
    val entries = items.map {
        PieEntry(
            it.value,
            it.label
        )
    }

    val dataSet = PieDataSet(entries, "").apply {
        colors = items.map { it.color.toArgb() }
        sliceSpace = 2f
        selectionShift = 0f
        valueTextColor = textColor.toArgb()
        valueTextSize = 12f
    }

    return PieData(dataSet).apply {
        setValueFormatter(PercentFormatter(chart))
    }
}

@Composable
private fun VehicleUtilizationHorizontalBarChart(
    items: List<DashboardChartItem>,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color
) {
    val chartTextColor = textColor.toArgb()
    val chartSecondaryTextColor = secondaryTextColor.toArgb()
    val chartGridColor = borderColor.toArgb()

    DashboardChartCard(
        title = "Vehicle utilization",
        subtitle = "Fleet status shown as percentage",
        cardColor = cardColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        borderColor = borderColor
    ) {
        if (items.isEmpty() || items.sumOf { it.value.toDouble() } == 0.0) {
            Text(
                text = "No vehicle data available yet.",
                color = secondaryTextColor,
                fontSize = 14.sp
            )
            return@DashboardChartCard
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            factory = { context ->
                HorizontalBarChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false

                    setDrawGridBackground(false)
                    setDrawBarShadow(false)
                    setDrawValueAboveBar(true)

                    setBackgroundColor(AndroidColor.TRANSPARENT)
                    setNoDataTextColor(textColor.toArgb())

                    setTouchEnabled(false)
                    isHighlightPerTapEnabled = false
                    isHighlightPerDragEnabled = false

                    axisRight.isEnabled = false

                    axisLeft.apply {
                        axisMinimum = 0f
                        axisMaximum = 100f
                        setTextColor(chartSecondaryTextColor)
                        setGridColor(chartGridColor)
                        valueFormatter = PercentAxisFormatter()
                    }

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        setDrawGridLines(false)
                        setTextColor(chartTextColor)
                        textSize = 11f
                        valueFormatter = IndexAxisValueFormatter(items.map { it.label })
                    }

                    data = createHorizontalBarData(
                        items = items,
                        textColor = textColor
                    )

                    setFitBars(true)
                    invalidate()
                }
            },
            update = { chart ->
                chart.axisLeft.setTextColor(chartSecondaryTextColor)
                chart.axisLeft.setGridColor(chartGridColor)
                chart.xAxis.setTextColor(chartTextColor)
                chart.data = createHorizontalBarData(
                    items = items,
                    textColor = textColor
                )
                chart.invalidate()
            }
        )
    }
}

private fun createHorizontalBarData(
    items: List<DashboardChartItem>,
    textColor: Color
): BarData {
    val entries = items.mapIndexed { index, item ->
        BarEntry(
            index.toFloat(),
            item.value
        )
    }

    val dataSet = BarDataSet(entries, "").apply {
        colors = items.map { it.color.toArgb() }
        valueTextColor = textColor.toArgb()
        valueTextSize = 12f
        valueFormatter = PercentValueFormatter()
    }

    return BarData(dataSet).apply {
        barWidth = 0.52f
    }
}

private class PercentAxisFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}%"
    }
}

private class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}%"
    }
}

@Composable
private fun DashboardChartCard(
    title: String,
    subtitle: String,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = secondaryTextColor,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}

@Composable
private fun ChartLegend(
    items: List<DashboardChartItem>,
    textColor: Color,
    secondaryTextColor: Color
) {
    val total = items.sumOf { it.value.toDouble() }.toFloat()

    Column {
        items.forEach { item ->
            val percentage = if (total == 0f) {
                0
            } else {
                ((item.value / total) * 100).toInt()
            }

            Row(
                modifier = Modifier.padding(vertical = 3.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp)
                ) {
                    drawCircle(color = item.color)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${item.label}: ${item.value.toInt()} ($percentage%)",
                    color = secondaryTextColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}