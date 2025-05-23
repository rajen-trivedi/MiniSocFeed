package com.rajen.minisocfeed.module

import com.rajen.minisocfeed.api.FeedRetrofitAPI
import com.rajen.minisocfeed.base.AppResponseConverter
import com.rajen.minisocfeed.model.FeedRequest
import com.rajen.minisocfeed.model.PostResponse
import io.reactivex.Single

class FeedRepository (
    private val feedRetrofitAPI: FeedRetrofitAPI
) {
    private val appResponseConverter: AppResponseConverter = AppResponseConverter()

    fun getPost(page: Int, feedRequest: FeedRequest): Single<PostResponse> {
        return feedRetrofitAPI.getPost(pageNo = page, feedRequest = feedRequest)
            .flatMap { appResponseConverter.convertToSingleWithFullResponse(it) }
    }
}