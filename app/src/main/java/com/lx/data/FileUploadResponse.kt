package com.lx.data


import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("output")
    val output: Output
) {
    data class Output(
        @SerializedName("filename")
        val filename: String,
        @SerializedName("header")
        val header: String
    )
}