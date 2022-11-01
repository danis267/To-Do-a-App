package com.example.to_do

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var button: Button
    private lateinit var textCreationDate: TextView
    private lateinit var editTitle: EditText
    private lateinit var editDetail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Add task"

        textCreationDate = findViewById(R.id.task_creation_date)
        textCreationDate.text = getTodayDate()

        editTitle = findViewById(R.id.task_name)
        editDetail = findViewById(R.id.task_detail)

        button = findViewById(R.id.datePickerButton)
        button.text = getTodayDate()

        initDatePicker()
    }

    private fun saveTask() {
        val title = editTitle.text.toString()
        val detail = editDetail.text.toString()
        val creationDate = textCreationDate.text.toString()
        val completionDate = button.text

        if (title.trim().isEmpty() || detail.trim().isEmpty()) {
            Toast.makeText(this@AddTaskActivity, "Please insert a title and detail", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent()
        intent.putExtra("taskTitle", title)
        intent.putExtra("taskDetail", detail)
        intent.putExtra("creationDate", creationDate)
        intent.putExtra("completionDate", completionDate)

        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    private fun getTodayDate(): String {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        var m = cal.get(Calendar.MONTH)
        m += 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(d, m, y)
    }

    private fun initDatePicker() {
        val dateSetListener: DatePickerDialog.OnDateSetListener = object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                var month = month
                month += 1
                val date: String = makeDateString(day, month, year)
                button.text = date
            }
        }
        val cal = Calendar.getInstance()
       // cal.set(Calendar.MINUTE, 5)
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog = DatePickerDialog(this, style, dateSetListener, y, m, d)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }
    private fun getMonthFormat(month: Int): String {
        if (month == 1)
            return "JAN"
        if (month == 2)
            return "FEB"
        if (month == 3)
            return "MAR"
        if (month == 4)
            return "APR"
        if (month == 5)
            return "MAY"
        if (month == 6)
            return "JUN"
        if (month == 7)
            return "JUL"
        if (month == 8)
            return "AUG"
        if (month == 9)
            return "SEP"
        if (month == 10)
            return "OCT"
        if (month == 11)
            return "NOV"
        if (month == 12)
            return "DEC"
        return "JAN"
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.save -> {
                saveTask()
               // Toast.makeText(this@AddTaskActivity, "Save task", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> finish()
        }
        return true
    }
}