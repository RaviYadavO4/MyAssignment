package com.example.myassignment.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.myassignment.data.db.dao.UserDao
import com.example.myassignment.data.db.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.inject

class AuthViewModel(
    private val googleSignInClient: GoogleSignInClient,
) : ViewModel() {

    var showLoader = MutableLiveData<Boolean>()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userDao: UserDao by inject(UserDao::class.java)

    fun signInWithGoogle(idToken: String) = liveData(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()

            // After successful login, save the user to Room database
            result.user?.let { user ->
                val newUser = User(
                    userId = user.uid,
                    displayName = user.displayName?:"",
                    email = user.email?:"",
                    photoUrl = user.photoUrl?.toString()?:""
                )
                userDao.clearAndInsert(newUser)

                emit(user) // Returning the user data
            }
        } catch (e: Exception) {
            showLoader.value = false
            emit(null) // handle error appropriately
        }
    }

    fun getGoogleSignInClient() = googleSignInClient
}
