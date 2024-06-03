package com.dicoding.submissionintermediate.respon

import com.google.gson.annotations.SerializedName

class ResgisResp (
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)