package com.evgeniykim.callrecord.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.databinding.CallerDetailsItemsBinding
import com.evgeniykim.callrecord.models.CallerDetailsModel

class CallerDetailsAdapter(var callerDetailsList: ArrayList<CallerDetailsModel>): RecyclerView.Adapter<CallerDetailsAdapter.MyViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CallerDetailsAdapter.MyViewHolder {
        val binding = CallerDetailsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallerDetailsAdapter.MyViewHolder, position: Int) {
        val caller_details = callerDetailsList[position]
        holder.binding.textDayCallerDetailsItems.text = caller_details.date
        holder.binding.nameCallerDetailsItems.text = caller_details.phoneNum
        if (caller_details.name!!.isNotEmpty()) {
            holder.binding.nameCallerDetailsItems.text = caller_details.name
        }
        if (caller_details.name == caller_details.secondName) {
            holder.binding.secondNameCallerDetailsItems.visibility = View.INVISIBLE
        }
        holder.binding.secondNameCallerDetailsItems.text = caller_details.secondName
        holder.binding.callTimeDetailsItems.text = caller_details.time
    }

    override fun getItemCount(): Int = callerDetailsList.size

    inner class MyViewHolder(val binding: CallerDetailsItemsBinding): RecyclerView.ViewHolder(binding.root)
}