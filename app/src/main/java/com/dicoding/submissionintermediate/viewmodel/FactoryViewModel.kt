package com.dicoding.submissionintermediate.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionintermediate.ViewModelMain
import com.dicoding.submissionintermediate.autentifikasi.ViewModelAuth
import com.dicoding.submissionintermediate.maps.ViewModelMaps
import com.dicoding.submissionintermediate.story.ViewModelStory
import com.dicoding.submissionintermediate.utils.Inject

class FactoryViewModel(private val pref: AutentifikasiPref, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewModelAuth::class.java) -> {
                ViewModelAuth(pref) as T
            }
            modelClass.isAssignableFrom(ViewModelStory::class.java) -> {
                ViewModelStory(pref) as T
            }
            modelClass.isAssignableFrom(ViewModelMain::class.java) -> {
                ViewModelMain(Inject.provideRepository(pref, context)) as T
            }
            modelClass.isAssignableFrom(ViewModelMaps::class.java) -> {
                ViewModelMaps(pref, Inject.provideRepository(pref, context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}