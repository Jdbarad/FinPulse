package com.innoveloper.finpulse.di

import com.innoveloper.finpulse.data.local.AppDatabase
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import com.innoveloper.finpulse.ui.screen.entry.ExpenseEntryViewModel
import com.innoveloper.finpulse.ui.screen.list.ExpenseListViewModel
import com.innoveloper.finpulse.ui.screen.report.ExpenseReportViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().expenseDao() }
    single { get<AppDatabase>().categoryDao() }

    singleOf(::ExpenseRepository)

    viewModelOf(::ExpenseListViewModel)
    viewModelOf(::ExpenseReportViewModel)
    viewModelOf(::ExpenseEntryViewModel)

}