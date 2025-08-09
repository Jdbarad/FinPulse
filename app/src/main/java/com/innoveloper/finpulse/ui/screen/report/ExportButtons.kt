package com.innoveloper.finpulse.ui.screen.report

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ExportButtons(onShare: (String) -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                Toast.makeText(context, "Simulating PDF Export...", Toast.LENGTH_SHORT).show()
                // In a real app, generate a file and then share it.
                onShare("This is your exported expense report (as plain text).")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT PDF (SIM)")
        }
        Button(
            onClick = {
                Toast.makeText(context, "Simulating CSV Export...", Toast.LENGTH_SHORT).show()
                // For simulation, we'll just generate text and share.
                onShare("Date,Amount,Category\n2025-08-07,150.0,Food\n2025-08-06,3500.0,Staff")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT CSV (SIM)")
        }
    }
}
