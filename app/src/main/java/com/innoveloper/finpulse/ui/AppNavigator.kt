package com.innoveloper.finpulse.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.innoveloper.finpulse.ui.screen.entry.ExpenseEntryRoute
import com.innoveloper.finpulse.ui.screen.list.ExpenseListRoute
import com.innoveloper.finpulse.ui.screen.report.ExpenseReportRoute

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "expense_list") {
        composable("expense_list") {
            ExpenseListRoute(
                goToReport = { navController.navigate("expense_report") },
                goToEntry = { navController.navigate("expense_entry") }
            )
        }
        composable("expense_entry") {
            ExpenseEntryRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("expense_report") {
            ExpenseReportRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}