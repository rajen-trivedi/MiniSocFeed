package com.rajen.minisocfeed.base

import com.rajen.minisocfeed.module.FeedRepository
import com.rajen.minisocfeed.ui.viewmodel.FeedsViewModel
import dagger.Module
import dagger.Provides

@Module
class AppViewModelProvider {
    @Provides
    fun provideFeedsViewModel(feedRepository: FeedRepository): FeedsViewModel {
        return FeedsViewModel(feedRepository)
    }
}