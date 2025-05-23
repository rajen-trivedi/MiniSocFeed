package com.rajen.minisocfeed.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.rajen.minisocfeed.R
import com.rajen.minisocfeed.base.MEDIA_URL
import com.rajen.minisocfeed.base.calendar
import com.rajen.minisocfeed.base.formatTo
import com.rajen.minisocfeed.base.messages
import com.rajen.minisocfeed.base.prettyCount
import com.rajen.minisocfeed.base.simpleDateFormat
import com.rajen.minisocfeed.databinding.FeedPostViewBinding
import com.rajen.minisocfeed.model.Post
import timber.log.Timber
import java.util.TimeZone

class PostFeedAdapter : ListAdapter<Post, PostFeedAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) 1 else -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            FeedPostViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: FeedPostViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postItem: Post) {
            with(binding) {
                val mediaList = postItem.media.orEmpty()
                val mediaAdapter = MediaViewPagerAdapter()
                mediaViewPager.adapter = mediaAdapter
                mediaAdapter.submitList(postItem.media?.toList())
                if (mediaList.size > 1) {
                    mediaCounter.isVisible = true
                    mediaCounter.text = "1/${mediaList.size}"
                    mediaViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            mediaCounter.text = "${position + 1}/${mediaList.size}"
                        }
                    })
                } else {
                    mediaCounter.isVisible = false
                }

                val profile = MEDIA_URL + postItem.user?.profile
                Glide.with(binding.root.context)
                    .load(profile)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(profileImage)

                userName.text =
                    postItem.user?.name?.ifEmpty { postItem.user.webName?.ifEmpty { "" } ?: "" }

                val userName = postItem.user?.name ?: ""
                val comment =
                    if (postItem.description.isNullOrEmpty()) "" else postItem.description.orEmpty()
                val userNameWithComment = buildSpannedString {
                    bold { append(userName) }
                    append(" ")
                    append(comment.ifEmpty { "" })
                }
                userCaption.text = userNameWithComment

                postItem.createAt?.let { createdAt ->
                    try {
                        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val date = simpleDateFormat.parse(createdAt)
                            ?.formatTo(TimeZone.getDefault(), simpleDateFormat) ?: return
                        calendar.time = simpleDateFormat.parse(date) ?: return
                        val timeMessage = TimeAgo.using(calendar.timeInMillis, messages)
                        Log.d("FeedPostView", "timeMessage: $timeMessage")
                        sendPostTime.text = timeMessage
                    } catch (e: Exception) {
                        Timber.tag("FeedPostViewException").e(e)
                    }
                }

                updateFeedLike(postItem)

                likesText.text = if ((postItem.totalLike ?: 0) > 1) "Likes" else "Like"

                likePost.setOnClickListener {
                    postItem.selfLike = postItem.selfLike != true
                    val totalLikes = postItem.totalLike
                    if (postItem.selfLike == true) {
                        val countLike = totalLikes?.let { it + 1 } ?: 0
                        postItem.totalLike = countLike
                        likesText.text = if ((postItem.totalLike ?: 0) > 1) "Likes" else "Like"
                        updateFeedLike(postItem)
                    } else {
                        val countLike = totalLikes?.let { it - 1 } ?: 0
                        postItem.totalLike = countLike
                        likesText.text = if ((postItem.totalLike ?: 0) > 1) "Likes" else "Like"
                        updateFeedLike(postItem)
                    }
                }
            }
        }

        private fun updateFeedLike(postItem: Post) {
            with(binding) {
                if (postItem.selfLike == true) {
                    likePost.setImageResource(R.drawable.ic_liked)
                } else {
                    likePost.setImageResource(R.drawable.ic_unliked)
                }

                val totalLikes = postItem.totalLike
                if (totalLikes != null) {
                    if (totalLikes >= 0) {
                        countLike.text = totalLikes.prettyCount().toString()
                    }
                }
            }
        }
    }

    private class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}