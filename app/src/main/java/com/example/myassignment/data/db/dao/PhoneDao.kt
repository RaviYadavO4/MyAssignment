package com.example.myassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.example.myassignment.data.db.entity.Phone
import kotlinx.coroutines.flow.Flow


@Dao
abstract class PhoneDao : BaseDao<Phone> {

    @Query("SELECT * FROM phone")
    abstract fun all(): Flow<List<Phone>>

    @Query("DELETE FROM phone")
    abstract fun clear(): Unit

    @Query("DELETE FROM phone WHERE slug = :slug")
    abstract fun deleteById(slug:String): Int

    @Delete
    abstract fun deletePhone(phone: Phone)

    @Transaction
    open fun clearAndInsert(categories: List<Phone>) {
        clear()
        insert(categories)
    }
}