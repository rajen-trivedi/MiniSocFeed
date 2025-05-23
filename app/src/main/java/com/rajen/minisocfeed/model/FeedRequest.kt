package com.rajen.minisocfeed.model

import com.google.gson.annotations.SerializedName

data class FeedRequest(
    @field:SerializedName("postIdList") val postIdList: ArrayList<Any>? = arrayListOf(),
    @field:SerializedName("feed_report_id") val shots: Boolean? = false
)
