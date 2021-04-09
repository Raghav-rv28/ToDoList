package com.example.todolist

import Database.TaskDatabase
import android.content.Context
import androidx.room.Room

private const val  DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context){


    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()
    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }
    }

    fun get(): TaskRepository{
        return INSTANCE ?:
                throw IllegalStateException("TaskRepoistory must be initialized")
    }
}