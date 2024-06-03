package com.dicoding.submissionintermediate.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.submissionintermediate.database.DatabaseStory
import com.dicoding.submissionintermediate.respon.StoryResp
import com.dicoding.submissionintermediate.retrofit.ApiService
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.ModelUser
import com.dicoding.submissionintermediate.viewmodel.Story


class StoryRepository(private val pref: AutentifikasiPref,
                      private val apiService: ApiService,
                      private val database: DatabaseStory) {

    suspend fun getStories(token: String): StoryResp {
        return apiService.getStories("Bearer $token")
    }

    fun getUser(): LiveData<ModelUser> {
        return pref.getUser().asLiveData()
    }

    suspend fun logout() {
        pref.logout()
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = RemoteStoryMediator(database, apiService, pref),
            pagingSourceFactory = {
                database.storyDao().getStory()
            }
        ).liveData
    }
}