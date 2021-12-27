package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()

    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify adapter that the data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
        //1. Detect when a user has clicked on the Add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Rolland","User clicked on button")
//        }
        loadItems()
        //look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input  field, so that user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //get a reference to the button
        //set an on click listener
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. grab the text the user entered into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. add the string to the list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            // notify adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)
            //3. reset the text field
            inputTextField.setText("")

            saveItems()
        }

    }
    //Save the data that the user has inputted
    //save by writing and reading from a file

    //get the file we need
    fun getDataFile(): File {

        //every line represents a specific task in listOfTasks
        return File(filesDir, "data.txt")
    }
    //load the items by reading every line in the data file
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
    //save items by writing them into the data file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch(ioException: IOException){
            ioException.printStackTrace()
        }

    }
}