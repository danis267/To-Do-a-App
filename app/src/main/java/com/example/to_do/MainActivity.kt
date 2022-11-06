package com.example.to_do

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var uid : String

    companion object {
        const val ADD_TASK_REQUEST = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid.toString()
        firebaseDatabase = FirebaseDatabase.getInstance()

        db = FirebaseFirestore.getInstance()
        preferenceManager = PreferenceManager(this@MainActivity)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        taskAdapter = TaskAdapter()
        recyclerView.adapter = taskAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ViewModel::class.java]

        viewModel.getAll().observe(this@MainActivity, Observer {
            taskAdapter.setData(it as ArrayList<Data>)
           // Toast.makeText(this@MainActivity, "onChanged", Toast.LENGTH_SHORT).show()
        })

        ItemTouchHelper(object : SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                viewModel.delete(taskAdapter.deleteTask(viewHolder.adapterPosition))

                Toast.makeText(this@MainActivity, "Task deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.delete -> {
                viewModel.deleteAll()
                Toast.makeText(this@MainActivity, "All task deleted", Toast.LENGTH_SHORT)
                    .show()
            }
            R.id.logout -> {
                firebaseAuth.signOut()
                Toast.makeText(
                    this@MainActivity,
                    "Logout successfully",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@MainActivity, SignIn::class.java))
                finish()
            }
            R.id.add -> {
                    val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
                    startActivityForResult(intent, ADD_TASK_REQUEST)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK && data != null ) {
            val taskTitle = data.getStringExtra("taskTitle")
            val taskDetail = data.getStringExtra("taskDetail")
            val creationDate = data.getStringExtra("creationDate")
            val completionDate = data.getStringExtra("completionDate")

            viewModel.insert(Data(taskTitle, taskDetail, completionDate, creationDate))

            firebaseDatabase.reference.child("users")
                .child(uid)
                .push()
                .setValue(Data(taskTitle, taskDetail, completionDate, creationDate)).addOnSuccessListener {

                }



            Toast.makeText(this@MainActivity, "Task saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Task not saved", Toast.LENGTH_SHORT).show()
        }
    }

}