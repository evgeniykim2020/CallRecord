package com.evgeniykim.callrecord.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.databinding.ContactsItemsBinding
import com.evgeniykim.callrecord.listeners.OnCallListener
import com.evgeniykim.callrecord.models.ContactsModel

class ContactsAdapter(private val contactList: ArrayList<ContactsModel>): RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private var onCallListener: OnCallListener<ContactsModel>? = null
    fun setCallListener(onCallListener: OnCallListener<ContactsModel>){
        this.onCallListener = onCallListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ContactsItemsBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.nameContacts.text = contact.name
        holder.binding.phoneNumber.text = contact.phone
        holder.binding.phoneCallContacts.setOnClickListener{
            if (onCallListener != null) {
                onCallListener!!.onCall(contactList[position])
            }
        }
//        notifyDataSetChanged()
    }

    override fun getItemCount() = contactList.size

    inner class MyViewHolder(val binding: ContactsItemsBinding): RecyclerView.ViewHolder(binding.root)
}