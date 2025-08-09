package com.innoveloper.finpulse.ui.screen.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.formatDate

@Composable
fun ExpenseListItem(expense: ExpenseWithCategory, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(expense.category.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                if (expense.expense.notes?.isNotEmpty() == true) {
                    Text(expense.expense.notes, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("₹${"%.2f".format(expense.expense.amount)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(formatDate(expense.expense.date), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}