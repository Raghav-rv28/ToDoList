package com.example.todolist

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.joda.time.LocalDate
import java.util.*


private const val TAG = "TaskFragment"
private const val TASK_ID_KEY = "task_id_key"
private const val DIALOG_DATE = "dialog_date"
private const val REQUEST_DATE = 0
class TaskFragment: Fragment(), DatePickerFragment.Listener {
    //Local
    private lateinit var task: Task

    private lateinit var tasktitle: EditText
    private lateinit var taskNotes: EditText
    private lateinit var taskLevel: EditText
    private lateinit var taskEtc: EditText
    private lateinit var dateButton: Button
    private lateinit var myDayButton: Button
    private lateinit var completedCheckBox: CheckBox

    private lateinit var background: LinearLayout

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId = arguments?.getSerializable(TASK_ID_KEY) as UUID
        Log.d(TAG, "Retrieved task id in onCreate: $taskId")
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onDateSelected(date: LocalDate) {
        task.date = date
        updateUI()
    }

    private fun updateUI() {
        tasktitle.setText(task.title)
        var curDate = task.date.toString("EEE, dd MMM")
        dateButton.text = curDate
        taskNotes.setText(task.notes)
        taskLevel.setText(task.ECL.toString())
        taskEtc.setText(task.ETC.toString())
        task.ECL?.let { background(it) }
        completedCheckBox.apply {
            isChecked = task.completed
            jumpDrawablesToCurrentState()
        }
    }

    private fun background(x: Int) {
        if(x == 1) background.setBackgroundColor(Color.parseColor("#80e64847"))
        if(x == 2) background.setBackgroundColor(Color.parseColor("#80fca041"))
        if(x == 3) background.setBackgroundColor(Color.parseColor("#8059eb00"))
        if(x == 4) background.setBackgroundColor(Color.parseColor("#8002e3ea"))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstance: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        //Local
        tasktitle = view.findViewById(R.id.editTextTitle) as EditText
        taskNotes = view.findViewById(R.id.editTextTime) as EditText
        taskLevel = view.findViewById(R.id.eisenhowerNumber) as EditText
        taskEtc = view.findViewById(R.id.expectedCompletionTime) as EditText
        dateButton = view.findViewById(R.id.button) as Button
        myDayButton = view.findViewById(R.id.mydaybutton) as Button
        completedCheckBox = view.findViewById(R.id.checkBox) as CheckBox
        background = view.findViewById(R.id.backGround) as LinearLayout
        dateButton.apply {
            text = task.date.toString()
            isEnabled = true
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(tag, "called onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { task ->
                    task?.let {
                        this.task = task
                        updateUI()
                    }
                }
        )
    }

    private fun changeLevel(xVal: String): Int {
        return try {
            Integer.parseInt(xVal).also { task.ECL = it }
        } catch (ex: Exception) {
            0.also { task.ECL = it }
        }
    }

    private fun setEtc(xVal: String):Int {
        return try {
            Integer.parseInt(xVal).also { task.ETC = it }
        } catch (ex: Exception) {
            0.also { task.ETC = it }
        }
    }

    override fun onStart() {
        super.onStart()
        //TITLE
        tasktitle.addTextChangedListener(object : TextChangeListener<EditText?>(taskNotes) {
            override fun onTextChanged(target: EditText?, s: CharSequence) {
                task.title = s.toString()
            }override fun afterTextChanged(target: EditText?, s: Editable) {}
        })

        //NOTES
        taskNotes.addTextChangedListener(object : TextChangeListener<EditText?>(taskNotes) {
            override fun onTextChanged(target: EditText?, s: CharSequence) {
                task.notes = s.toString()
            }override fun afterTextChanged(target: EditText?, s: Editable) {}
        })

        //EISENHOWER LEVEL
        taskLevel.addTextChangedListener(object : TextChangeListener<EditText?>(taskLevel) {
            override fun afterTextChanged(target: EditText?, s: Editable) {
                changeLevel(s.toString())
            }
            override fun onTextChanged(target: EditText?, s: CharSequence) {}
        })

        taskEtc.addTextChangedListener(object :TextChangeListener<EditText?>(taskEtc) {
            override fun afterTextChanged(target: EditText?, s: Editable){
                setEtc(s.toString())
            }override fun onTextChanged(target: EditText?, s: CharSequence) {}
        })

        //TASK DONE OR NOT
        completedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                task.completed = isChecked
                Log.i(TAG, "Task Completed: $task.completed")
            }
        }

        //MY DAY BUTTON
        myDayButton.setOnClickListener {
            if(myDayButton.text.equals("Add to My Day"))
            {
                val dateToday = LocalDate()
                task.date = dateToday
                myDayButton.text = "Added to My Day"
                dateButton.text = dateToday.toString("EEE, dd MMM")
            }
             else if(myDayButton.text.equals("Added to My Day")){
                 task.date = LocalDate(0)
                myDayButton.text = "Add to My Day"
                dateButton.text = "Add Due Date"
            }
            updateUI()
        }

        // ADD A DUE DATE
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(task.date).apply {
                setTargetFragment(this@TaskFragment, REQUEST_DATE)
                show(this@TaskFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }
        override fun onStop() {
            super.onStop()
            saveData()
        }
        fun saveData(){
            taskDetailViewModel.saveTask(task)
            Toast.makeText(requireContext(), "Successfully Saved!", Toast.LENGTH_SHORT).show()
        }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_task_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            taskDetailViewModel.deleteTask(this.task)
            Toast.makeText(
                    requireContext(),
                    "Successfully removed: ${this.task.title}",
                    Toast.LENGTH_SHORT).show()
            fragmentManager!!.beginTransaction().remove(this).commitAllowingStateLoss();
            val fragment = TaskListFragment.newInstance()
            fragmentManager!!
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${this.task.title}?")
        builder.setMessage("Are you sure you want to delete ${this.task.title}?")
        builder.create().show()
    }
        companion object {
            fun newInstance(taskId: UUID): TaskFragment {
                return TaskFragment().apply {
                    arguments = newArguments(taskId)
                }
            }

            @VisibleForTesting
            fun newArguments(taskId: UUID) = Bundle().apply {
                putSerializable(TASK_ID_KEY, taskId)
            }
        }

    }