package com.example.todolist

import Database.TaskDatabase
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val  DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context){

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDAO = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks(): LiveData<List<Task>> = taskDAO.getTasks()

    fun getTask(id: UUID): LiveData<Task?> = taskDAO.getTask(id)

    fun updateTask(task: Task) {
        executor.execute {
            taskDAO.updateTask(task)
        }
    }

    fun addTask(task: Task) {
        executor.execute {
            taskDAO.addTask(task)
        }
    }

    fun deleteTask(task: Task) {
        executor.execute {
            taskDAO.deleteTask(task)
        }
    }

    fun getTasksSortedDateAsc(): LiveData<List<Task>> =  taskDAO.getTasksSortedDateAsc()
    fun getTasksSortedDateDesc(): LiveData<List<Task>>  = taskDAO.getTasksSortedDateDesc()

    fun getTasksSortedLevelAsc(): LiveData<List<Task>>  = taskDAO.getTasksSortedLevelAsc()
    fun getTasksSortedLevelDesc(): LiveData<List<Task>>  = taskDAO.getTasksSortedLevelDesc()


    fun getTasksSortedEtcAsc(): LiveData<List<Task>>  = taskDAO.getTasksSortedEtcAsc()
    fun getTasksSortedEtcDesc(): LiveData<List<Task>>  = taskDAO.getTasksSortedEtcDesc()

    fun findTaskByDate(targetDate: Date): LiveData<List<Task>>  = taskDAO.findTaskByDate(targetDate)

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}