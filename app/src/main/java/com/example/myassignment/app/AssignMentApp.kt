package com.example.myassignment.app

import android.app.Application
import android.content.Context
import com.example.myassignment.di.appModule
import com.example.myassignment.di.databaseModule
import com.example.myassignment.di.preferencesModule
import com.example.myassignment.di.repoModule
import com.example.myassignment.di.retrofitModule
import com.example.myassignment.di.viewModelModule
import com.example.myassignment.persistence.Prefs
import com.example.myassignment.theme.ThemeHelper
import com.example.myassignment.utils.C
import com.google.firebase.FirebaseApp
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AssignMentApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val themeHelper = ThemeHelper(Prefs(this.getSharedPreferences(C.PREFS_FILE_NAME, Context.MODE_PRIVATE)))
        themeHelper.applySavedTheme()
        setupDI()
        setupConnectionBuddy()


    }

    private fun setupConnectionBuddy() {

        val networkInspectorConfiguration = ConnectionBuddyConfiguration.Builder(this)
            .setNotifyImmediately(false)
            .build()

        ConnectionBuddy.getInstance().init(networkInspectorConfiguration)
    }
    private fun setupDI() {

        startKoin {
            androidContext(this@AssignMentApp)
            modules(
                listOf(
                    preferencesModule,
                    appModule,
                    databaseModule,
                    retrofitModule,
                    repoModule,
                    viewModelModule
                )
            )
        }
    }
}