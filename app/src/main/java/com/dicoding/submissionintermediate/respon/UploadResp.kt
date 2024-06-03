package com.dicoding.submissionintermediate.respon

import com.google.gson.annotations.SerializedName

data class UploadResp(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)