package com.rajen.minisocfeed.base

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import kotlin.reflect.KClass

class NetworkException(
    val error: Any?,
    message: String = "",
    val exception: Throwable
) : RuntimeException(message, exception) {

    companion object {

        private fun parseError(
            retrofit: Retrofit,
            httpException: HttpException,
            kClass: KClass<*>
        ): Any? {
            if (httpException.response()?.isSuccessful == true) {
                return null
            }
            val errorBody = httpException.response()?.errorBody() ?: return null
            val converter: Converter<ResponseBody, Any> =
                retrofit.responseBodyConverter(kClass.java, arrayOf())
            return converter.convert(errorBody)
        }

        fun asRetrofitException(
            annotations: Array<Annotation>,
            retrofit: Retrofit,
            throwable: Throwable
        ): Throwable {
            val errorType: ErrorType? = annotations.find { it is ErrorType } as? ErrorType

            return if (errorType != null && throwable is retrofit2.HttpException) {
                val error = parseError(retrofit, throwable, errorType.type)
                NetworkException(error, throwable.message(), throwable)
            } else throwable
        }
    }
}