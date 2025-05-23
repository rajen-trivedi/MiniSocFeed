package com.rajen.minisocfeed.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("status") val status: String?= null,
    @SerializedName("data") val data: List<PostDataItem>? = emptyList(),
    @SerializedName("totalCount") val totalCount: Int?= null,
    @SerializedName("currentPage") val currentPage: Int?= null,
    @SerializedName("message") val message: String?= null
)

data class PostDataItem(
    @SerializedName("post") val post: Post? = null,
    @SerializedName("sponsor") val sponsor: List<Sponsor>? = emptyList()
)

data class Post(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("userId") val user: User? = User(),
    @SerializedName("description") val description: String? = null,
    @SerializedName("media") val media: List<Media>? = emptyList(),
    @SerializedName("postType") val postType: String? = null,
    @SerializedName("createAt") val createAt: String? = null,
    @SerializedName("TotalLike") var totalLike: Int? = null,
    @SerializedName("selfLike") var selfLike: Boolean? = null,
    @SerializedName("shareCount") val shareCount: Int? = null,
    @SerializedName("comments") val comments: Int? = null
)

data class User(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: Long? = null,
    @SerializedName("profile") val profile: String? = null,
    @SerializedName("userType") val userType: String? = null,
    @SerializedName("contactStatus") val contactStatus: Boolean? = null,
    @SerializedName("webName") val webName: String? = null
)

data class Media(
    @SerializedName("url") val url: String? = null,
    @SerializedName("aspectRatio") val aspectRatio: Double? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("_id") val id: String? = null
)

data class Sponsor(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("groupName") val groupName: String? = null,
    @SerializedName("groupProfile") val groupProfile: String? = null,
    @SerializedName("groupDescription") val groupDescription: String? = null,
    @SerializedName("memberLength") val memberLength: Int? = null,
    @SerializedName("storyCount") val storyCount: Int? = null,
    @SerializedName("isSeenStory") val isSeenStory: Boolean? = null
)

