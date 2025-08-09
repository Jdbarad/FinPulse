package com.innoveloper.finpulse.ui.screen.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innoveloper.finpulse.data.local.Expense
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExpenseEntryViewModel(
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {

    val categories = expenseRepository.allCategories
    val todayExpenses: Flow<List<ExpenseWithCategory>> = expenseRepository.getExpensesForToday()
    val totalSpentToday: Flow<Double> = todayExpenses.map { expenses ->
        expenses.sumOf { it.expense.amount }
    }

    fun addExpense(expense: Expense) = viewModelScope.launch {
        expenseRepository.addExpense(expense)
    }

    fun addCategory(categoryName: String) = viewModelScope.launch {
        expenseRepository.addCategory(categoryName)
    }


}