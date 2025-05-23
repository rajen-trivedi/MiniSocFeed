package com.rajen.minisocfeed.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajen.minisocfeed.ui.viewmodel.FeedsViewModel
import com.rajen.minisocfeed.ui.viewmodel.FeedsViewState
import com.rajen.minisocfeed.MyApplication
import com.rajen.minisocfeed.R
import com.rajen.minisocfeed.base.ViewModelFactory
import com.rajen.minisocfeed.base.getViewModelFromFactory
import com.rajen.minisocfeed.base.subscribeAndObserveOnMainThread
import com.rajen.minisocfeed.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postFeedAdapter: PostFeedAdapter

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<FeedsViewModel>
    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this)
        feedsViewModel = getViewModelFromFactory(viewModelFactory)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listenToViewModel()
        listenToView()
        feedsViewModel.pullToRefresh(true)
    }

    private fun listenToView() {
        postFeedAdapter = PostFeedAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.postsView.layoutManager = layoutManager
        binding.postsView.adapter = postFeedAdapter
        binding.postsView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 5) {
                        feedsViewModel.loadMore()
                    }
                }
            }
        })
    }

    private fun listenToViewModel() {
        feedsViewModel.feedState.subscribeAndObserveOnMainThread { state ->
            when (state) {
                is FeedsViewState.LoadingState -> {
                    if (feedsViewModel.pageNumber == 1) {
                        binding.shimmerLayout.isVisible = true
                        binding.shimmerLayout.startShimmer()
                        binding.postsView.isVisible = false
                        binding.noFeedTxt.isVisible = false
                    }
                }
                is FeedsViewState.GetFeedList -> {
                    val posts = state.feedInfoList.mapNotNull { it.post }
                    if (feedsViewModel.pageNumber == 1 && posts.isEmpty()) {
                        binding.noFeedTxt.isVisible = true
                        binding.postsView.isVisible = false
                    } else {
                        binding.noFeedTxt.isVisible = false
                        binding.postsView.isVisible = true
                        postFeedAdapter.submitList(posts.toList())
                    }
                }
                is FeedsViewState.ErrorMessage -> {
                    if (feedsViewModel.pageNumber == 1) {
                        binding.noFeedTxt.isVisible = true
                        binding.postsView.isVisible = false
                    }
                }
            }
        }
    }
}