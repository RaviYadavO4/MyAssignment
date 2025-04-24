package com.example.myassignment.utils


sealed class Resource<out T : Any?> {
    data class Success<out T : Any?>(val data: T?) : Resource<T>()
    data class Error<out T : Any?>(val exception: Exception, val data: T?) : Resource<T>()

    data class Loading<out T : Any?>(val data: T?) : Resource<T>()
}