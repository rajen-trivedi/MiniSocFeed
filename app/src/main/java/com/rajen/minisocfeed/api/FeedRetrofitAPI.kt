package com.rajen.minisocfeed.api

import com.rajen.minisocfeed.model.FeedRequest
import com.rajen.minisocfeed.model.PostResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import io.reactivex.Single

interface FeedRetrofitAPI {

    @POST("v2/post/getPost")
    fun getPost(
        @Query("page") pageNo: Int,
        @Body feedRequest: FeedRequest
    ): Single<PostResponse>
}