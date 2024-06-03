package com.dicoding.submissionintermediate.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "remote_keys")
data class RemoteKey (
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)