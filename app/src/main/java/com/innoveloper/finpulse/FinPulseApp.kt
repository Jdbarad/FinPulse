package com.innoveloper.finpulse

import android.app.Application
import com.innoveloper.finpulse.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class FinPulseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FinPulseApp)
            modules(appModule)
        }
    }

}