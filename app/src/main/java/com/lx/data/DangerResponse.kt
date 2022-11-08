package com.lx.data


import com.google.gson.annotations.SerializedName

data class DangerResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("LAT")
        val LAT: String,
        @SerializedName("LNG")
        val LNG: Double,
        @SerializedName("LAT2")
        val LAT2: Double
    )
}