package com.example.myassignment.di

import android.content.Context
import com.example.myassignment.R
import com.example.myassignment.notification.FcmService
import com.example.myassignment.theme.ThemeHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { provideGoogleSignInClient(get()) }

    single { ThemeHelper(get()) }
    single {


        FcmService(
            context = get(), // You must declare `androidContext()` in your Koin setup
            serviceAccountFileName = "assignment-7d384-firebase-adminsdk-fbsvc-8d21d29e30.json",
            projectId = "assignment-7d384"
        )
    }
}

fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))  // from Firebase Console
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, googleSignInOptions)
}