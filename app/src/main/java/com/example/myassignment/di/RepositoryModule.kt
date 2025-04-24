package com.example.myassignment.di


import com.example.myassignment.repository.PhoneRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repoModule = module {

    single {
        PhoneRepository(
            apiService = get(qualifier = named("default")),
            phoneDao = get()
        )
    }

}