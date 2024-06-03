package com.dicoding.submissionintermediate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submissionintermediate.story.StoryRepository
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.ModelUser
import com.dicoding.submissionintermediate.viewmodel.Story
import kotlinx.coroutines.launch

class ViewModelMain(private val repository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)

    fun getUser(): LiveData<ModelUser> {
        return repository.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}