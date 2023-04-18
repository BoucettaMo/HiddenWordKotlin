package com.example.hiddenwordkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val activity: MainActivity,
    val list: MutableList<String>,

): RecyclerView.Adapter<MyAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.model,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount()= list.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int)    {
        holder.textView.text=list[position]

    }

}
