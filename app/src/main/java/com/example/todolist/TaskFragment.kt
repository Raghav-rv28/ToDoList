package com.example.todolist

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val TAG = "TaskFragment"
private const val TASK_ID_KEY = "task_id_key"
private const val DIALOG_DATE = "dialog_date"
private const val REQUEST_DATE = 0
class TaskFragment: Fragment(), DatePickerFragment.Listener {
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

    override fun onDateSelected(date: Date) {
        task.date = date
        updateUI()
    }

    private fun updateUI() {
        tasktitle.setText(task.title)
        dateButton.text = task.date.toString()
        taskNotes.setText(task.notes)
        taskLevel.setText(task.ECL.toString())
        taskEtc.setText(task.ETC.toString())
        background(task.ECL)
        completedCheckBox.apply {
            isChecked = task.completed
            jumpDrawablesToCurrentState()
        }
    }

    fun background(x: Int) {
        if(x == 1) background.setBackgroundColor(Color.parseColor("#75FF0000"))
        if(x == 2) background.setBackgroundColor(Color.parseColor("#75FFA500"))
        if(x == 3) background.setBackgroundColor(Color.parseColor("#7590EE90"))
        if(x == 4) background.setBackgroundColor(Color.parseColor("#750000FF"))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstance: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

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
                task.date = Date()
                myDayButton.text = "Added to My Day"
                dateButton.text = task.date.toString()
            }
             else if(myDayButton.text.equals("Added to My Day")){
                 task.date = Date(0, 0, 0)
                myDayButton.text = "Add to My Day"
                dateButton.text = "Add Due Date"
            }
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
            taskDetailViewModel.saveTask(task)
            Toast.makeText(requireContext(), "Successfully Saved!", Toast.LENGTH_SHORT).show()
        }

        companion object {
            fun newInstance(crimeId: UUID): TaskFragment {
                return TaskFragment().apply {
                    arguments = newArguments(crimeId)
                }
            }

            @VisibleForTesting
            fun newArguments(crimeId: UUID) = Bundle().apply {
                putSerializable(TASK_ID_KEY, crimeId)
            }
        }

    }