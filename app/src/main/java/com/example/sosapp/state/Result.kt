package com.example.sosapp.state

sealed class Result {
    object Success : Result()
    object Failed : Result()
}