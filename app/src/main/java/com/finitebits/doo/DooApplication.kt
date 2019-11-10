package com.finitebits.doo

import android.app.Application
import com.finitebits.doo.data.TodoRepository
import timber.log.Timber

class DooApplication : Application() {
    val todoRepository : TodoRepository
        get() = ServiceLocator.provideTodoRepository(this)

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}