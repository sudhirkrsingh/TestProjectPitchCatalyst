package com.pitchcatalyst.testproject.presentation.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pitchcatalyst.testproject.R
import com.pitchcatalyst.testproject.common.ItemClickListener
import com.pitchcatalyst.testproject.data.DataReq
import com.pitchcatalyst.testproject.databinding.ActivityMainBinding
import com.pitchcatalyst.testproject.presentation.adapter.ContentAdapter
import com.pitchcatalyst.testproject.presentation.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity(), ItemClickListener {


    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContentAdapter
    private val selectedItems = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = ContentAdapter(emptyList(), this) // Initialize with an empty list
        binding.recyclerView.adapter = adapter

        binding.btnDelete.setOnClickListener {
            viewModel.deleteItems(selectedItems)
            adapter.dataSetChange()
        }


        // Observe the SnackBar event
        viewModel.getSnackBarEvent().observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                showSnackBar(message)
            }
        })

        viewModel.itemsLiveData.observe(this, Observer { value ->
            adapter.setItems(value)

        })


    }

    private fun showSnackBar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCheckboxClick(item: DataReq, isChecked: Boolean) {
            // Handle checkbox click in the activity
        if (isChecked) {
            // Add the item to your list or perform any other action
            item.key?.let { selectedItems.add(it) }
        } else {
            // Remove the item from your list or perform any other action
            selectedItems.remove(item.key)
        }
    }


}