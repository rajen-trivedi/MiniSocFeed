package com.rajen.minisocfeed.base

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass
import com.rajen.minisocfeed.base.NetworkException.Companion.asRetrofitException

@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*> = AppResponse::class)

internal class RxErrorHandlingCallAdapterFactory private constructor(private val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) :
    CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        @Suppress("UNCHECKED_CAST")
        val rxJava2CallAdapter: CallAdapter<Any, Any> =
            rxJava2CallAdapterFactory.get(
                returnType,
                annotations,
                retrofit
            ) as CallAdapter<Any, Any>
        return RxCallAdapterWrapper(annotations, retrofit, rxJava2CallAdapter)
    }

    internal class RxCallAdapterWrapper<R>(
        private val annotations: Array<Annotation>,
        private val retrofit: Retrofit,
        private val wrapped: CallAdapter<R, *>
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrapped.adapt(call)) {
                is Single<*> -> result.onErrorResumeNext(Function { throwable ->
                    Single.error(
                        asRetrofitException(
                            annotations,
                            retrofit,
                            throwable
                        )
                    )
                })
                is Observable<*> -> result.onErrorResumeNext(Function { throwable ->
                    Observable.error(
                        asRetrofitException(
                            annotations,
                            retrofit,
                            throwable
                        )
                    )
                })
                is Completable -> result.onErrorResumeNext(Function { throwable ->
                    Completable.error(
                        asRetrofitException(
                            annotations,
                            retrofit,
                            throwable
                        )
                    )
                })
                else -> result
            }

        }
    }

    companion object {
        fun createAsync(): RxErrorHandlingCallAdapterFactory {
            return RxErrorHandlingCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        }
    }
}