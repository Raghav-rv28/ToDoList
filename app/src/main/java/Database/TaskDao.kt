package Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>
    
    @Update
    fun updateTask(task: Task)
    
    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    //DUE DATE
    @Query("SELECT * FROM task ORDER BY date asc")
    fun getTasksSortedDateAsc(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY date desc")
    fun getTasksSortedDateDesc(): LiveData<List<Task>>

    //LEVEL
    @Query("SELECT * FROM task ORDER BY ecl asc")
    fun getTasksSortedLevelAsc(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY ecl desc")
    fun getTasksSortedLevelDesc(): LiveData<List<Task>>

    //Etc
    @Query("SELECT * FROM task ORDER BY etc asc")
    fun getTasksSortedEtcAsc(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY etc desc")
    fun getTasksSortedEtcDesc(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE date = :targetDate")
    fun findTaskByDate(targetDate: Date): LiveData<List<Task>>


}
