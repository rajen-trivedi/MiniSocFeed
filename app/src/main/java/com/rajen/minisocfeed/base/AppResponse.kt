package com.rajen.minisocfeed.base

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppResponse(
    @field:SerializedName("success")
    val success: Boolean = false,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("code")
    val code: String? = null,
) {
    val safeErrorMessage: String
        get() = message ?: "Something went wrong. Please try after sometime"
}