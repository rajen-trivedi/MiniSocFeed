package com.rajen.minisocfeed.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.rajen.minisocfeed.base.MEDIA_IMAGE_URL
import com.rajen.minisocfeed.base.MEDIA_VIDEO_URL
import com.rajen.minisocfeed.databinding.ItemMediaImageBinding
import com.rajen.minisocfeed.databinding.ItemMediaVideoBinding
import com.bumptech.glide.request.target.Target
import com.rajen.minisocfeed.R
import com.rajen.minisocfeed.model.Media

class MediaViewPagerAdapter :
    ListAdapter<Media, RecyclerView.ViewHolder>(MediaDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_IMAGE = 1
        private const val VIEW_TYPE_VIDEO = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            "Image" -> VIEW_TYPE_IMAGE
            "Video" -> VIEW_TYPE_VIDEO
            else -> VIEW_TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val binding =
                    ItemMediaImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ImageViewHolder(binding)
            }

            VIEW_TYPE_VIDEO -> {
                val binding =
                    ItemMediaVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return VideoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaItem = getItem(position)
        when (holder) {
            is ImageViewHolder -> holder.bind(mediaItem)
            is VideoViewHolder -> holder.bind(mediaItem)
        }
    }

    inner class ImageViewHolder(private val binding: ItemMediaImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: Media) {
            val circularProgressDrawable = CircularProgressDrawable(binding.postImageView.context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                setColorSchemeColors(Color.RED)
                start()
            }
            val profile = MEDIA_IMAGE_URL + mediaItem.thumbnail

            Glide.with(binding.root.context)
                .load(profile)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.img_image_placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(binding.postImageView)
        }
    }

    inner class VideoViewHolder(private val binding: ItemMediaVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: Media) {
            val circularProgressDrawable = CircularProgressDrawable(binding.postImageView.context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                setColorSchemeColors(Color.RED)
                start()
            }
            val profile = MEDIA_VIDEO_URL + mediaItem.thumbnail
            Glide.with(binding.root.context)
                .load(profile)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.img_image_placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(binding.postImageView)
        }
    }

    class MediaDiffCallback : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }
}
