package com.example.to_do

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var arrayList = ArrayList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = arrayList[position]

        holder.title.text = data.title
        holder.detail.text = data.details
        holder.createdDate.text = data.createTask
        holder.completionDate.text = data.completeTaskDate
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(arrayList: ArrayList<Data>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int): Data {
        return arrayList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val title: TextView = itemView.findViewById(R.id.title)
         val detail: TextView = itemView.findViewById(R.id.Detail)
         val createdDate: TextView = itemView.findViewById(R.id.Create_Date)
         val completionDate: TextView = itemView.findViewById(R.id.Completion_Date)
    }

}