package com.interview.test

import android.app.Application
import com.interview.test.di.networkModule
import com.interview.test.di.repositoryModule
import com.interview.test.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class CardsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@CardsApp)
            androidLogger(Level.DEBUG)
            modules(
                listOf(
                    repositoryModule,
                    viewModelModule,
                    networkModule
                )
            )
        }
    }
}