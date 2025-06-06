package com.example.myassignment.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myassignment.persistence.Prefs
import com.example.myassignment.utils.C
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {

    single {
        providePreferences(androidContext())
    }

    single {
        Prefs(prefs = get())
    }
}

private fun providePreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(C.PREFS_FILE_NAME, Context.MODE_PRIVATE)