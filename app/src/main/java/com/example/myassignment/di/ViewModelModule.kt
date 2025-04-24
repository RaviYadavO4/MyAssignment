package com.example.myassignment.di

import com.example.myassignment.ui.auth.AuthViewModel
import com.example.myassignment.ui.image.ImageViewModel
import com.example.myassignment.ui.phone.PhoneViewModel
import com.example.myassignment.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { AuthViewModel(get()) }
    viewModel { PhoneViewModel(get(),get(),get()) }
    viewModel { ImageViewModel() }
    viewModel { ProfileViewModel(get(),get(),get()) }

}