package com.dicoding.submissionintermediate.respon

import com.dicoding.submissionintermediate.viewmodel.Story
import com.google.gson.annotations.SerializedName

data class StoryResp (
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("listStory")
    val listStory: List<Story>,
    @field:SerializedName("message")
    val message: String
)