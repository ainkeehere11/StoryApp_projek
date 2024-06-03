package com.dicoding.submissionintermediate.autentifikasi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediate.respon.LoginResp
import com.dicoding.submissionintermediate.respon.LoginResult
import com.dicoding.submissionintermediate.respon.ResgisResp
import com.dicoding.submissionintermediate.retrofit.ApiConfig
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.ModelUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelAuth(private val pref: AutentifikasiPref) : ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _loginUser = MutableLiveData<LoginResult>()
    val loginUser: LiveData<LoginResult> = _loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerUser = MutableLiveData<ResgisResp>()
    val registerUser: LiveData<ResgisResp> = _registerUser

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResp> {
            override fun onResponse(call: Call<LoginResp>, response: Response<LoginResp>) {
                val responseBody = response.body()
                _isLoading.value = false
                if(response.isSuccessful && responseBody != null){
                    _loginUser.value = responseBody.loginResult
                    _loginStatus.value = true
                    val token = responseBody.loginResult.token
                    setToken(token)
                } else{
                    _loginStatus.value = false
                }
            }
            override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                _isLoading.value = false
                _loginStatus.value = false
            }
        })
    }

    fun register(name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<ResgisResp> {
            override fun onResponse(
                call: Call<ResgisResp>,
                response: Response<ResgisResp>
            ) {
                _isLoading.value = false
                Log.e("This is auth", response.body().toString())
                if (response.isSuccessful) {
                    _registerUser.value = ResgisResp(response.isSuccessful, response.body()?.message ?: "Register success")
                } else {
                    _registerUser.value = ResgisResp(response.isSuccessful, "Register failed")
                    Log.e("This is auth", "onFailureResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResgisResp>, t: Throwable) {
                _isLoading.value = false
                Log.e("This is auth", "onFailureThrowable: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<ModelUser> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: LoginResult) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun setToken(token: String) {
        viewModelScope.launch {
            pref.setToken(token)
        }
    }
}