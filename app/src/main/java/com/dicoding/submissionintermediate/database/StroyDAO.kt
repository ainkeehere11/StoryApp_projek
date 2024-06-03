package com.dicoding.submissionintermediate.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.submissionintermediate.viewmodel.Story

@Dao
interface StroyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStory(listStoryItem: Story)

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getStory(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteStory()
}