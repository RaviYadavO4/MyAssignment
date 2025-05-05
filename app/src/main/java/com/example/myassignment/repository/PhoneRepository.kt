package com.example.myassignment.repository


import com.aawaz.data.util.RateLimiter
import com.example.myassignment.data.db.dao.PhoneDao
import com.example.myassignment.data.db.entity.Phone
import com.example.myassignment.data.maper.toPhone
import com.example.myassignment.network.AssignmentApiServices
import com.hadiyarajesh.flower.Resource
import com.hadiyarajesh.flower.networkBoundResource

import com.zplesac.connectionbuddy.ConnectionBuddy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit



const val FETCH_KEY_PHONES = "phones"

class PhoneRepository(
    private val apiService: AssignmentApiServices,
    private val phoneDao: PhoneDao
) {

    private val repoPhonesRateLimit = RateLimiter<String>(30, TimeUnit.SECONDS)

    @ExperimentalCoroutinesApi
    fun fetchPhones(): Flow<Resource<List<Phone>>> {
        return networkBoundResource(
            fetchFromLocal = { phoneDao.all() },
            shouldFetchFromRemote = {
                val isConnected = ConnectionBuddy.getInstance().hasNetworkConnection()
                val rateLimiter = repoPhonesRateLimit.shouldFetch(FETCH_KEY_PHONES)

                val fetch = isConnected and rateLimiter

                fetch
            },
            fetchFromRemote = { apiService.getPhoneData() },
            processRemoteResponse = {},
            saveRemoteData = {

                val categories = it.toPhone()
                if (categories.isNotEmpty()) {
                    phoneDao.clearAndInsert(categories)
                }
            },
            onFetchFailed = { errorBody, statusCode ->
                repoPhonesRateLimit.reset(FETCH_KEY_PHONES)
            }
        ).flowOn(Dispatchers.IO)
    }
}