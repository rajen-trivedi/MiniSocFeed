package com.rajen.minisocfeed

import android.app.Application
import com.rajen.minisocfeed.di.AppComponent
import com.rajen.minisocfeed.di.MiniSocAppModule
import com.rajen.minisocfeed.di.DaggerAppComponent

class MyApplication : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .miniSocAppModule(MiniSocAppModule(this))
            .build()

        appComponent.inject(this)
    }
}