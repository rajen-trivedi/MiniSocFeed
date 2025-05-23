package com.rajen.minisocfeed.base

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class AppInterceptorHeaders : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
        if (token.isNotEmpty()) {
            if (getAPIBaseUrl().contains(original.url.host)) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
        }

        val response: Response
        try {
            response = chain.proceed(requestBuilder.build())
        } catch (t: Throwable) {
            Timber.e("error in InterceptorHeaders:\n${t.message}")
            throw IOException(t.message)
        }
        return response
    }
}