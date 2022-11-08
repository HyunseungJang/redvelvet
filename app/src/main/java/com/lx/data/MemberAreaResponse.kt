package com.lx.data


import com.google.gson.annotations.SerializedName

data class MemberAreaResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("header")
    val header: Header
) {
    data class Data(
        @SerializedName("id")
        val id: String,
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lng")
        val lng: Double
    )
    data class Header(
        @SerializedName("total")
        val total: Int
    )
}