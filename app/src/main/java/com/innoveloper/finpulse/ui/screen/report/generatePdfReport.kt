package com.innoveloper.finpulse.ui.screen.report

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.compose.ui.graphics.Paint
import androidx.core.content.FileProvider
import com.innoveloper.finpulse.data.local.Category
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.formatDate
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Generates a PDF file from the report data and saves it to a cache directory.
 */
fun Context.generatePdfReport(
    dailyTotals: Map<Long, Double>,
    categoryTotals: Map<Category, Double>
): File {
    // 1. Create a PdfDocument
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 page size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // 2. Define paint styles for drawing text
    val titlePaint = Paint().asFrameworkPaint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 18f
        color = android.graphics.Color.BLACK
    }
    val headerPaint = Paint().asFrameworkPaint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 14f
        color = android.graphics.Color.BLACK
    }
    val bodyPaint = Paint().asFrameworkPaint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 12f
        color = android.graphics.Color.DKGRAY
    }

    // 3. Draw the content onto the canvas
    var yPosition = 40f
    val leftMargin = 40f

    canvas.drawText("Expense Report (Last 7 Days)", leftMargin, yPosition, titlePaint)
    yPosition += 40f

    // Daily Totals Section
    canvas.drawText("Daily Totals", leftMargin, yPosition, headerPaint)
    yPosition += 25f
    if (dailyTotals.isEmpty()) {
        canvas.drawText("No data available.", leftMargin, yPosition, bodyPaint)
        yPosition += 20f
    } else {
        dailyTotals.forEach { (dateMillis, total) ->
            canvas.drawText("${formatDate(dateMillis)}: ₹${"%.2f".format(total)}", leftMargin, yPosition, bodyPaint)
            yPosition += 20f
        }
    }

    // Category Totals Section
    yPosition += 20f
    canvas.drawText("Category Totals", leftMargin, yPosition, headerPaint)
    yPosition += 25f
    if (categoryTotals.isEmpty()) {
        canvas.drawText("No data available.", leftMargin, yPosition, bodyPaint)
    } else {
        categoryTotals.forEach { (category, total) ->
            canvas.drawText("${category.name}: ₹${"%.2f".format(total)}", leftMargin, yPosition, bodyPaint)
            yPosition += 20f
        }
    }

    // 4. Finish the page and write to a file
    pdfDocument.finishPage(page)

    val reportsDir = File(cacheDir, "reports")
    if (!reportsDir.exists()) {
        reportsDir.mkdirs()
    }
    val file = File(reportsDir, "FinPulse_Report.pdf")
    FileOutputStream(file).use {
        pdfDocument.writeTo(it)
    }
    pdfDocument.close()
    return file
}


/**
 * Generates a CSV file from the list of expenses.
 */
fun Context.generateCsvFile(expenses: List<ExpenseWithCategory>): File {
    val csvDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val csvContent = buildString {
        // Header
        append("Date,Category,Title,Amount,Notes\n")
        // Rows
        expenses.forEach { item ->
            val date = csvDateFormat.format(Date(item.expense.date))
            val category = item.category.name
            val title = "\"${item.expense.title.replace("\"", "\"\"")}\""
            val amount = "%.2f".format(item.expense.amount)
            val notes = "\"${item.expense.notes?.replace("\"", "\"\"") ?: ""}\""
            append("$date,$category,$title,$amount,$notes\n")
        }
    }

    val reportsDir = File(cacheDir, "reports")
    if (!reportsDir.exists()) {
        reportsDir.mkdirs()
    }
    val file = File(reportsDir, "FinPulse_Expenses.csv")
    file.writeText(csvContent)
    return file
}

/**
 * Creates and triggers a share intent for the given file.
 */
fun Context.shareFile(file: File, mimeType: String) {
    // Use the authority you defined in AndroidManifest.xml
    val authority = "${packageName}.provider"
    val uri = FileProvider.getUriForFile(this, authority, file)

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = mimeType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(shareIntent, "Share Report via"))
}