package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDate
import java.util.*
@Entity
data class Task(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var date: LocalDate = LocalDate(),
                var notes: String = "",
                var ECL: Int = 1, //between 1-4 EISENHOWER COMPLEX
                //var tags: List<String> = mutableListOf<String>(),
                var ETC: Int = 0, // in minutes EXPECTED TIME IN COMPLETION
                var completed: Boolean = false)