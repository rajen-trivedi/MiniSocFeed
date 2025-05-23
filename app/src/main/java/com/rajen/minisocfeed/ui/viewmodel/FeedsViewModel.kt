package com.rajen.minisocfeed.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.rajen.minisocfeed.module.FeedRepository
import com.rajen.minisocfeed.base.subscribeOnIoAndObserveOnMainThread
import com.rajen.minisocfeed.model.FeedRequest
import com.rajen.minisocfeed.model.PostDataItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class FeedsViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val feedStateSubject: PublishSubject<FeedsViewState> = PublishSubject.create()
    val feedState: Observable<FeedsViewState> = feedStateSubject.hide()

    private var listOfFeedData: MutableList<PostDataItem> = mutableListOf()
    var pageNumber: Int = 1
    private var isLoadMore: Boolean = true
    private var isLoading: Boolean = false

    fun pullToRefresh(isLoad: Boolean) {
        pageNumber = 1
        isLoadMore = true
        isLoading = false
        listOfFeedData.clear()
        getAllFeed(isLoad)
    }

    private fun getAllFeed(isLoad: Boolean) {
        feedRepository.getPost(pageNumber, FeedRequest())
            .doOnSubscribe {
                feedStateSubject.onNext(FeedsViewState.LoadingState(isLoad))
            }
            .doAfterTerminate {
                isLoading = false
                feedStateSubject.onNext(FeedsViewState.LoadingState(false))
            }
            .subscribeOnIoAndObserveOnMainThread({ response ->
                if(response?.status == "200") {
                    response.data?.let {
                        if (pageNumber == 1) {
                            listOfFeedData = response.data.toMutableList()
                            feedStateSubject.onNext(FeedsViewState.GetFeedList(listOfFeedData))
                        } else {
                            if (it.isNotEmpty()) {
                                listOfFeedData.addAll(it)
                                feedStateSubject.onNext(FeedsViewState.GetFeedList(listOfFeedData))
                            } else {
                                isLoadMore = false
                            }
                        }
                    }
                } else {
                    feedStateSubject.onNext(FeedsViewState.ErrorMessage(response?.message ?: ""))
                }
            }, { throwable ->
                throwable.printStackTrace()
                throwable.localizedMessage?.let {
                    feedStateSubject.onNext(FeedsViewState.ErrorMessage(it))
                }
            })
    }

    fun loadMore() {
        if (!isLoading) {
            isLoading = true
            if (isLoadMore) {
                pageNumber++
                getAllFeed(false)
            }
        }
    }
}

sealed class FeedsViewState {
    data class LoadingState(val isLoading: Boolean) : FeedsViewState()
    data class GetFeedList(val feedInfoList: MutableList<PostDataItem>) : FeedsViewState()
    data class ErrorMessage(val errorMessage: String) : FeedsViewState()
}