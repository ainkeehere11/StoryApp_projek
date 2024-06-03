package com.dicoding.submissionintermediate.viewmodel

data class ModelUser(
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean
)
