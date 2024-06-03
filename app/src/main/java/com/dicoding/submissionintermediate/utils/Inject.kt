package com.dicoding.submissionintermediate.utils

import android.content.Context
import com.dicoding.submissionintermediate.database.DatabaseStory
import com.dicoding.submissionintermediate.retrofit.ApiConfig
import com.dicoding.submissionintermediate.story.StoryRepository
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref

object Inject {
    fun provideRepository(pref: AutentifikasiPref, context: Context): StoryRepository {
        val storyDatabase = DatabaseStory.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(pref, apiService, storyDatabase )
    }
}