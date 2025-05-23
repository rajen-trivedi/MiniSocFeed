package com.rajen.minisocfeed.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.rajen.minisocfeed.model.PostResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class AppResponseConverter {
    fun convertToSingleWithFullResponse(postResponse: PostResponse?): Single<PostResponse> {
        return when {
            postResponse == null -> Single.error(Exception("Failed to process the info"))
            postResponse.status != "200" -> Single.error(Exception(postResponse.message ?: "Unknown error occurred"))
            else -> Single.just(postResponse)
        }
    }
}

fun <T> SingleEmitter<T>.onSafeError(throwable: Exception) {
    if (!isDisposed) onError(throwable)
}

fun <T : Any> SingleEmitter<T>.onSafeSuccess(t: T) {
    if (!isDisposed) onSuccess(t)
}

fun <T> Single<T>.subscribeOnIoAndObserveOnMainThread(
    onNext: (t: T) -> Unit,
    onError: (Throwable) -> Unit
): Disposable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)
}

inline fun <reified T : ViewModel> AppCompatActivity.getViewModelFromFactory(vmFactory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, vmFactory)[T::class.java]
}

fun <T> Observable<T>.subscribeAndObserveOnMainThread(onNext: (t: T) -> Unit): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext)
}

fun Date.formatTo(timeZone: TimeZone, formatter: SimpleDateFormat): String {
    formatter.timeZone = timeZone
    return formatter.format(this)
}

val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
val calendar: Calendar = Calendar.getInstance()
private var localeByLanguageTag: Locale = Locale.forLanguageTag("en")
var messages: TimeAgoMessages = TimeAgoMessages.Builder().withLocale(localeByLanguageTag).build()

fun Int.prettyCount(): String? {
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val numValue: Long = this.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.00").format(numValue / 10.0.pow(base * 3.toDouble())) + suffix[base]
    } else {
        DecimalFormat().format(numValue)
    }
}

const val MEDIA_URL = "https://d3b13iucq1ptzy.cloudfront.net/"
const val MEDIA_IMAGE_URL = "https://d3b13iucq1ptzy.cloudfront.net/uploads/posts/image/"
const val MEDIA_VIDEO_URL = "https://d3b13iucq1ptzy.cloudfront.net/uploads/posts/video/thumb/"