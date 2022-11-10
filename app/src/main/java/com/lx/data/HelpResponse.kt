package com.lx.data


import com.google.gson.annotations.SerializedName

data class HelpResponse(
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
        @SerializedName("LAT")
        val LAT: Double,
        @SerializedName("LNG")
        val LNG: Double,
        @SerializedName("LAT2")
        val LAT2: Double
    )
    data class Header(
        @SerializedName("total")
        val total: Int
    )
}