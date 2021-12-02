package com.evgeniykim.callrecord.fragments


import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.evgeniykim.callrecord.adapters.ContactsAdapter
import com.evgeniykim.callrecord.databinding.FragmentContactsBinding
import com.evgeniykim.callrecord.listeners.OnCallListener
import com.evgeniykim.callrecord.listeners.Utility
import com.evgeniykim.callrecord.models.ContactsModel
import kotlinx.android.synthetic.main.fragment_contacts.*


class Contacts: Fragment(), OnCallListener<ContactsModel> {

    private val PERMISSIONS_REQUEST = 100

//    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentContactsBinding.inflate(inflater, container, false)

        binding.RVContacts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

//    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loadContacts()
        showContacts()
    }


    private fun showContacts() {

        val adapter = ContactsAdapter(getContacts())
        RVContacts.adapter = adapter
        adapter.setCallListener(this)

    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun loadContacts() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
//                requireContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            requireActivity().requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE), PERMISSIONS_REQUEST)
//        } else {
//
//            val adapter = ContactsAdapter(getContacts())
//            RVContacts.adapter = adapter
//            adapter!!.setCallListener(this)
//        }
//        }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == PERMISSIONS_REQUEST) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                loadContacts()
//            } else {
//                Toast.makeText(requireContext(), "Нужно дать разрешения", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun getContacts(): ArrayList<ContactsModel> {
        val contacts = ArrayList<ContactsModel>()
        val cursor = requireContext().contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = requireContext().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if (cursorPhone!!.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contacts.add(ContactsModel(name, phoneNumValue))
                        }
                    }
                    cursorPhone.close()
                }
            }

        } else {
            Toast.makeText(requireContext(), "Нет контактов", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        return contacts
    }

    override fun onCall(contact: ContactsModel) {
        Utility.makeCall(requireContext(), contact.phone)
    }


}

