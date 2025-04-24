package com.example.myassignment.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
        @PrimaryKey(autoGenerate = false)
        val id: Int = 1,
        val userId: String,
        val displayName: String,
        val email: String,
        val photoUrl: String,
) : Parcelable

