package com.innoveloper.finpulse.data.repository

import com.innoveloper.finpulse.data.local.Category
import com.innoveloper.finpulse.data.local.CategoryDao
import com.innoveloper.finpulse.data.local.Expense
import com.innoveloper.finpulse.data.local.ExpenseDao
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    val allExpenses: Flow<List<ExpenseWithCategory>> = expenseDao.getAllExpenses()

    fun getExpensesForToday(): Flow<List<ExpenseWithCategory>> {
        val calendar = Calendar.getInstance()
        val startOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        val endOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis
        return expenseDao.getExpensesForDateRange(startOfDay, endOfDay)
    }

    fun getExpensesForLast7Days(): Flow<List<ExpenseWithCategory>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis
        return expenseDao.getExpensesForDateRange(startDate, endDate)
    }

    fun getExpensesForDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getExpensesForDateRange(startDate, endDate)
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun addCategory(categoryName: String) {
        categoryDao.insertCategory(Category(name = categoryName))
    }

}