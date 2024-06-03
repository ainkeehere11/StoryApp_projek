package com.dicoding.submissionintermediate.retrofit

import com.dicoding.submissionintermediate.respon.LoginResp
import com.dicoding.submissionintermediate.respon.ResgisResp
import com.dicoding.submissionintermediate.respon.StoryResp
import com.dicoding.submissionintermediate.respon.UploadResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name")name:String,
        @Field("email")email:String,
        @Field("password")password:String,
    ): Call<ResgisResp>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")email:String,
        @Field("password")password:String,
    ): Call<LoginResp>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String
    ): Call<StoryResp>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UploadResp>


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): StoryResp

    @GET("stories")
    suspend fun listStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResp
}