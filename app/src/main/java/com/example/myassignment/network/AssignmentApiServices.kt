package com.example.myassignment.network

import com.example.myassignment.data.remote.AssignmentResponse
import com.hadiyarajesh.flower.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface AssignmentApiServices {


    @GET("objects")
     fun getPhoneData(
    ): Flow<ApiResponse<List<AssignmentResponse>>>


}