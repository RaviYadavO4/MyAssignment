package com.example.myassignment.data.maper

import com.example.myassignment.data.db.entity.Phone
import com.example.myassignment.data.remote.AssignmentResponse



fun List<AssignmentResponse>.toPhone(): List<Phone> {

    return mapIndexed { _, data ->
        Phone(
            slug = data.id,
            name = data.name,
            data = data.data.toString(),
        )
    }
}