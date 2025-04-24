package com.example.myassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.myassignment.data.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<User> {

    @Query("SELECT * FROM user WHERE id = 1  limit 1")
    abstract fun fetchUserPref(): Flow<User>


    @Query("DELETE FROM user")
    abstract fun clear(): Unit

    @Transaction
    open fun clearAndInsert(user: User) {
        clear()
        insert(user)
    }
}

