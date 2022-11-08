package com.lx.data


import com.google.gson.annotations.SerializedName

data class MemberListResponse(
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
        @SerializedName("pw")
        val pw: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("birth")
        val birth: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("phone")
        val phone: Int,
        @SerializedName("height")
        val height: String,
        @SerializedName("weight")
        val weight: String,
        @SerializedName("emernum")
        val emernum: String,
        @SerializedName("medicine")
        val medicine: String,
        @SerializedName("disease")
        val disease: String,
        @SerializedName("bloodtype")
        val bloodtype: String,
        @SerializedName("certificate")
        val certificate: String,
        @SerializedName("others")
        val others: String
    )
    data class Header(
        @SerializedName("total")
        val total: Int
    )
}