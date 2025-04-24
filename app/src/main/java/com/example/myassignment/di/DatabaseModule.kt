package com.example.myassignment.di

import android.content.Context
import androidx.room.Room
import com.example.myassignment.data.db.AssignmentDb
import com.example.myassignment.data.db.dao.PhoneDao
import com.example.myassignment.data.db.dao.UserDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AssignmentDb> {
        assignmentDb(androidContext = androidContext())
    }
    single { userDao(get()) }
    single { phoneDao(get()) }

}


private fun userDao(db: AssignmentDb): UserDao = db.userDao()
private fun phoneDao(db: AssignmentDb): PhoneDao = db.phoneDao()


private fun assignmentDb(androidContext: Context): AssignmentDb {
    return Room
        .databaseBuilder(androidContext, AssignmentDb::class.java, "assignment.db")
        .fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .fallbackToDestructiveMigrationFrom(
            1
        )
        .build()
}