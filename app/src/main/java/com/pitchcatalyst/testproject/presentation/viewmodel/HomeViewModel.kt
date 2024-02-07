package com.pitchcatalyst.testproject.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.pitchcatalyst.testproject.common.Event
import com.pitchcatalyst.testproject.data.DataReq
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    var titleContent = MutableLiveData<String>(null)
    var bodyContent = MutableLiveData<String>(null)
    private val snackBarEvent = MutableLiveData<Event<String>>()




    // Expose LiveData to observe SnackBar events
    fun getSnackBarEvent(): LiveData<Event<String>> = snackBarEvent

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Data")

    val itemsLiveData: MutableLiveData<List<DataReq>> = MutableLiveData()

    init {
        // Read operation
        viewModelScope.launch {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = mutableListOf<DataReq>()
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(DataReq::class.java)
                        item?.let { items.add(it) }
                    }
                    itemsLiveData.value = items
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    showSnackBar("Database Error: $error")
                }
            })

        }

    }


    fun addItem() {
        viewModelScope.launch {

            val data = DataReq()
            data.title = titleContent.value.toString()
            data.body = bodyContent.value.toString()
            if (data.title.isNullOrEmpty()) {
                showSnackBar("Please enter a title")
            } else if (data.body.isNullOrEmpty()) {
                showSnackBar("Please enter a body")
            } else if (data.title.isNullOrEmpty() && data.body.isNullOrEmpty()) {
                showSnackBar("Please enter both a title and a body")
            } else if (!data.title.isNullOrEmpty() && !data.body.isNullOrEmpty()) {
                val newItemRef = databaseReference.push()
                data.key = newItemRef.key // This is the unique identifier (key) for the new item
                newItemRef.setValue(data)
                titleContent.value = ""
                bodyContent.value = ""
                showSnackBar("Data has been added successfully")

            } else {
                // do nothing
            }
        }
    }

    // Method to trigger the SnackBar event
    private fun showSnackBar(message: String) {
        snackBarEvent.value = Event(message)
    }

    // Delete operation
    fun deleteItems(itemIds: List<String>) {
        for (itemId in itemIds) {
            databaseReference.child(itemId).removeValue()
        }
    }
}