package com.rajen.minisocfeed.di

import android.app.Application
import com.rajen.minisocfeed.ui.MainActivity

abstract class BaseUiApp : Application() {
    abstract fun setAppComponent(baseAppComponent: BaseAppComponent)
    abstract fun getAppComponent(): BaseAppComponent
}

interface BaseAppComponent {
    fun inject(app: Application)
    fun inject(mainActivity: MainActivity)
}

fun BaseUiApp.getComponent(): BaseAppComponent {
    return this.getAppComponent()
}