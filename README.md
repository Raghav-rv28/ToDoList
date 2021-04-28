
**To-Do list**

  To-Do List is an application that allows  you to organize your day by letting you add different
  tasks that needs to done by you. Organizing your daily tasks with a list would make everything
  much more manageable. By prioritizing the tasks planning  the order in which we have to do your 
  tasks, so that we can tell what needs your immediate attention, and what we can leave until 
  later.


**Features**:

1.Adding a task 

2.deleting a task

3.sorting task based on :
  
     -due date 
  
     -priority level
  
     -ETC

4.Calender view 

5.detailed task view 

6.adding notes to the task 



**WORKING**   

The project is allows you to add a task ,delete a particular task ,select the date for 
the particular task , enter the expected time to complete the task  and also allows to 
enter the priority level .Also has a sorting function that allows to sort different 
tasks with different priority level , due date .

Also works on the concept of Eisenhower complex. It uses the concept :

-Do the work 

-Decide 

-Delegate

-Delete 
making sure that we are not only just completing the tasks but also doing them in the 
right order . 

 

![1__rxIYhMJtDFAMu3YaXWxQQ](https://user-images.githubusercontent.com/76601342/115810510-03ed6200-a3bc-11eb-9882-471f37d46963.png)

***TaskDao***

Through class *TaskDao* we import data from the database through the repository 
First intializes the data then extracts the data from the database and through 
*ViewModel* class populates the task fragment and the task list fragment  


***View Model Class***

ViewModel is responsible for preparing and managing the data for an activity 
or fragment. It also handles the communication of the activity with the rest of 
the application

'
    
    val taskRepository =  TaskRepository.get()
    val tasksListLiveData = taskRepository.getTasks()
    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }
'

***TaskTypeConverter***

It converts the data type from the database and also extracts the data from the database 
For example:

' 

      @TypeConverter
     fun fromDate(date: Date?): Long? {
         return date?.time }
       @TypeConverter
         fun toDate(millisSinceEpoch: Long?): Date? {
             return millisSinceEpoch?.let{ Date(it)}
         }
     
     '


Coversion of time from int to total miliseconds. This type of conversion is done by the 
TaskTypeconverter  

    
***Fragments***

There are 3 main fragmnets :*TaskFragmnet* , *TaskListFragment* and *CalenderFragment* .
In  the main activity these fragments are populated and changed accordingly 

***Recycler view ***

It has two inner class:*TaskHolder* , *TaskAdapter* 
Task adapter takes in the data and populates it . The function bind takes and list 
data and binds it to the Recyclerview . The TaskAdapter tiggers it so that it bind 
 the data . 
 For example 
 
 ' 
      
      dateTextView.text = date
          notesImgView.visibility = if (!task.notes.equals("")) {
               View.VISIBLE
          } else {
          View.GONE
          }
   '
 
 Adapter class  tiggers the holder class and then the holder class that populates 
the data .
 
 ***Files***
 ![alt text](![2021-04-28 (2)](https://user-images.githubusercontent.com/76601342/116447090-9c08a280-a825-11eb-9f0c-d80b8209f3cf.png)
)
