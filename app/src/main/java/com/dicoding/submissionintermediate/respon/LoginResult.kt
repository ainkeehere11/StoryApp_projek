package com.dicoding.submissionintermediate.respon

import com.google.gson.annotations.SerializedName

class LoginResult (
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String,
    val isLogin: Boolean
)