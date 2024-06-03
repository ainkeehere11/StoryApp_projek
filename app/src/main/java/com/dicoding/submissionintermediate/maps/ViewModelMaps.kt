package com.dicoding.submissionintermediate.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediate.story.StoryRepository
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.Story
import kotlinx.coroutines.launch

class ViewModelMaps (private val pref: AutentifikasiPref,
                     private val repository: StoryRepository
                    ): ViewModel() {
    private val _listStories = MutableLiveData<List<Story>>()
    val listStories: LiveData<List<Story>> = _listStories

    fun getToken(): LiveData<String> = pref.getToken().asLiveData()

    fun listStory(token: String) {
        viewModelScope.launch {
            val listStory = repository.getStories(token)
            _listStories.value = listStory.listStory
        }
    }

}