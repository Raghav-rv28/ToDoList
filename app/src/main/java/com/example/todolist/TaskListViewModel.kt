package com.example.todolist

import androidx.lifecycle.ViewModel

class TaskListViewModel: ViewModel(){
    val tasks= mutableListOf<Task>()

    init {
        for(i in 0 until 100){
            val task= Task()
            task.title = "Task #$i"
            task.completed = i % 2 ==0
            if(i%2==0)
                task.notes = "this task has a note"
            tasks+=task
        }
    }
}