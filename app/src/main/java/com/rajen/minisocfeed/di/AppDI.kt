package com.rajen.minisocfeed.di

import android.app.Application
import android.content.Context
import com.rajen.minisocfeed.module.FeedModule
import com.rajen.minisocfeed.MyApplication
import com.rajen.minisocfeed.base.AppViewModelProvider
import com.rajen.minisocfeed.base.NetworkModule
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MiniSocAppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }
}

@Singleton
@Component(
    modules = [
        MiniSocAppModule::class,
        AppViewModelProvider::class,
        NetworkModule::class,
        FeedModule::class
    ]
)

interface AppComponent : BaseAppComponent {
    fun inject(app: MyApplication)
}