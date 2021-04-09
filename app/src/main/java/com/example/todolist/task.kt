package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.Date
@Entity
data class Task(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var date: Date = Date(),
                var notes: String = "",
                var ECL: Int = 2, //between 1-4 EISENHOWER COMPLEX
                //var tags: List<String> = mutableListOf<String>(),
                var ETC: Int = 30, // in minutes EXPECTED TIME IN COMPLETION
                var completed: Boolean = false)