package com.lx.data


import com.google.gson.annotations.SerializedName

data class CommunityResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("picture")
        val picture: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("area")
        val area: String
    )
}