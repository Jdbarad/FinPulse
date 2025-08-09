package com.innoveloper.finpulse.ui.screen.report

import androidx.lifecycle.ViewModel
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseReportViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val last7DaysExpenses: Flow<List<ExpenseWithCategory>> = expenseRepository.getExpensesForLast7Days()


}