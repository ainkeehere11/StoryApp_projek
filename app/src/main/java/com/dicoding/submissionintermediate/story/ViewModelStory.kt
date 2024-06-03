package com.dicoding.submissionintermediate.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediate.respon.StoryResp
import com.dicoding.submissionintermediate.respon.UploadResp
import com.dicoding.submissionintermediate.retrofit.ApiConfig
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.ModelUser
import com.dicoding.submissionintermediate.viewmodel.Story
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelStory(private val pref: AutentifikasiPref) : ViewModel() {

    private val _listStories = MutableLiveData<List<Story>>()
    val listStories: LiveData<List<Story>> = _listStories

    private val _uploadStories = MutableLiveData<UploadResp>()
    val uploadStories: LiveData<UploadResp> = _uploadStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        const val TAG = "ViewModelStory"
    }

    fun getStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStory("Bearer ${token}")
        client.enqueue(object : Callback<StoryResp> {
            override fun onResponse(
                call: Call<StoryResp>,
                response: Response<StoryResp>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listStories.value = responseBody.listStory
                } else {
                    Log.e(TAG, "OnFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResp>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun uploadStory(token: String, file:MultipartBody.Part, description:RequestBody, lat: Double? = null, lon: Double? = null) {
        _isLoading.value = true
        Log.e("Error", token.toString())
        val client = ApiConfig.getApiService().uploadImage("Bearer ${token}", file, description)
        client.enqueue(object : Callback<UploadResp> {
            override fun onResponse(
                call: Call<UploadResp>,
                response: Response<UploadResp>
            ) {
                if (response.isSuccessful) {
                    _uploadStories.postValue(response.body())
                    _isLoading.value = false
                    Log.e("Hasil", "Upload berhasil")
                } else {
                    Log.e(TAG, "OnFailure : ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResp>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })

    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

}