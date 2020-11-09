package com.example.threelines.Data

import java.sql.Date

data class Record (
    val record_id: Int,
    val title: String,
    val uploaded_date: String,
    val people: Int,
    val location: String
)