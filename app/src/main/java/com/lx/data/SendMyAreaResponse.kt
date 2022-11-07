package com.lx.data


import com.google.gson.annotations.SerializedName

data class SendMyAreaResponse(
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
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lng")
        val lng: Double
    )
}