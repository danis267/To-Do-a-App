//package com.example.to_do
//
//import android.app.DatePickerDialog
//import android.app.DatePickerDialog.OnDateSetListener
//
//
//private fun initDatePicker() {
//    val dateSetListener =
//        OnDateSetListener { datePicker, year, month, day ->
//            var month = month
//            month += 1
//            val date: String = makeDateString(day, month, year)
//            dateButton.setText(date)
//        }
//    val cal: Calendar = Calendar.getInstance()
//    val year: Int = cal.get(Calendar.YEAR)
//    val month: Int = cal.get(Calendar.MONTH)
//    val day: Int = cal.get(Calendar.DAY_OF_MONTH)
//    val style: Int = AlertDialog.THEME_HOLO_LIGHT
//    datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
//    //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//}