package com.example.myassignment.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myassignment.data.db.dao.UserDao
import com.example.myassignment.data.db.entity.User
import com.example.myassignment.persistence.Prefs
import com.example.myassignment.theme.ThemeHelper
import com.google.firebase.auth.FirebaseAuth
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao,private val themeHelper: ThemeHelper,private val prefs: Prefs) : ViewModel()  {

    val isDarkMode = MutableLiveData(themeHelper.isDarkMode())

    fun toggleTheme(isDark: Boolean) {
        themeHelper.setDarkMode(isDark)
        isDarkMode.value = isDark
    }
    var showLoader = MutableLiveData<Boolean>()
    private val _ueser = MutableLiveData<Resource<User>>()

    val user: LiveData<Resource<User>>
        get() = _ueser

    init {
        fetchUsers()
    }


    private fun fetchUsers() {

        viewModelScope.launch {

            showLoader.value = true
            try {
                userDao.fetchUserPref().collectLatest { user ->
                    showLoader.value = false
                    if (user != null) {
                        _ueser.postValue(Resource.success(user))
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun enableNotifications(enable: Boolean) {
        prefs.setNotificationsEnabled(enable)
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getIsNotificationsEnabled()
    }

    fun logout(){
        prefs.clear()
        FirebaseAuth.getInstance().signOut()
    }
}