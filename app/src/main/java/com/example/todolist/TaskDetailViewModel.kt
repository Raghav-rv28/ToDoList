package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TaskDetailViewModel: ViewModel(){
    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
            Transformations.switchMap(taskIdLiveData) { taskId ->
                TaskRepository.get().getTask(taskId)
            }

    fun loadTask(taskId: UUID) {
        taskIdLiveData.value = taskId
    }

    fun saveTask(task: Task) {
        TaskRepository.get().updateTask(task)
    }
    fun deleteTask(task: Task) {
        TaskRepository.get().deleteTask(task)
    }
}