package com.example.myassignment.ui.phone


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myassignment.data.db.dao.PhoneDao
import com.example.myassignment.data.db.entity.Phone
import com.example.myassignment.notification.FcmService
import com.example.myassignment.repository.PhoneRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class PhoneViewModel(private val repo: PhoneRepository, private val phoneDao: PhoneDao,private val fcmService: FcmService) : ViewModel() {

    var showLoader = MutableLiveData<Boolean>()
    private val _phones = MutableLiveData<Resource<List<Phone>>>()

    val phones: LiveData<Resource<List<Phone>>>
        get() = _phones

    private val _fcmToken = MutableLiveData<String>()
    val fcmToken: LiveData<String> = _fcmToken


    init {
        fetchCategories()
    }

     fun deletePhone(phone: Phone,fcmToken: String) {

         showLoader.value= true
         viewModelScope.launch(Dispatchers.IO) {
             try {
                 phoneDao.deleteById(phone.slug)
                 fcmService.sendNotificationToToken(
                     token = fcmToken,
                     title = "Item Deleted",
                     message = "${phone.name} was removed."
                 )

             }catch (e:Exception){
                 e.printStackTrace()
             }
         }
         showLoader.value= false
    }



    private fun fetchCategories() {

        viewModelScope.launch {

            showLoader.value = true
            try {
                repo.fetchPhones().map {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            _phones.postValue(Resource.loading(null))
                        }
                        Resource.Status.SUCCESS -> {
                            showLoader.value = false
                            val phones = it.data
                            _phones.postValue(Resource.success(phones))
                        }
                        Resource.Status.ERROR -> {
                            showLoader.value = false
                            _phones.postValue(Resource.error(it.message!!, null))
                        }
                    }
                }.collect()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

     fun fetchFcmToken() {
         FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
             Log.d("FCM_TOKEN", token)
         }
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _fcmToken.value = task.result
                } else {
                    Log.e("FCM", "Token fetch failed", task.exception)
                }
            }
    }

}
