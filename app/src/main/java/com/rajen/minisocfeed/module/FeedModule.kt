package com.rajen.minisocfeed.module

import com.rajen.minisocfeed.api.FeedRetrofitAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class FeedModule {

    @Provides
    @Singleton
    fun provideFeedRetrofitAPI(retrofit: Retrofit): FeedRetrofitAPI {
        return retrofit.create(FeedRetrofitAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedRepository(
        feedRetrofitAPI: FeedRetrofitAPI
    ): FeedRepository {
        return FeedRepository(feedRetrofitAPI)
    }
}