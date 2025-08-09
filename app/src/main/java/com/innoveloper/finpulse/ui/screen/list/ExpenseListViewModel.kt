package com.innoveloper.finpulse.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseListViewModel(
    expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _currentDateFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val currentDateFilter: StateFlow<DateFilterType> = _currentDateFilter.asStateFlow()

    val expenses: StateFlow<List<ExpenseWithCategory>> = _currentDateFilter.flatMapLatest { filterType ->
            when (filterType) {
                DateFilterType.Today -> expenseRepository.getExpensesForToday()
                DateFilterType.Week -> expenseRepository.getExpensesForLast7Days()
                DateFilterType.All -> expenseRepository.allExpenses
                is DateFilterType.CustomRange -> {
                    val endOfDay = filterType.endDate + (24 * 60 * 60 * 1000 - 1)
                    expenseRepository.getExpensesForDateRange(filterType.startDate, endOfDay)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Sets a predefined filter like Today, Week, or All.
     */
    fun setDateFilter(filterType: DateFilterType) {
        _currentDateFilter.value = filterType
    }
}