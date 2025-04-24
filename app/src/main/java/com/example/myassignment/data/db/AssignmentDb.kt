package com.example.myassignment.data.db

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.example.myassignment.data.db.dao.PhoneDao
import com.example.myassignment.data.db.dao.UserDao
import com.example.myassignment.data.db.entity.Phone
import com.example.myassignment.data.db.entity.User


@Database(
    entities = [
        User::class,
        Phone::class,
    ],
    version = 2,
    exportSchema = false
)

abstract class AssignmentDb : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneDao

    @Transaction
    suspend fun clearAll() {
        try {
            userDao().clear()
            phoneDao().clear()
        } catch (ex: Exception) {
            Log.v("exception","$ex clear all table ex ")
        }
    }
}
