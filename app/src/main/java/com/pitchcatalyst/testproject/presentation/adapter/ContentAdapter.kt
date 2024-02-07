package com.pitchcatalyst.testproject.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pitchcatalyst.testproject.common.ItemClickListener
import com.pitchcatalyst.testproject.data.DataReq
import com.pitchcatalyst.testproject.databinding.ItemContentBinding

class ContentAdapter(private var items: List<DataReq>,private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataReq, itemClickListener: ItemClickListener) {
            binding.tvTitle.text = item.title
            binding.tvBody.text = item.body
            binding.checkbox.isChecked = item.isChecked

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                itemClickListener.onCheckboxClick(item, isChecked)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position],itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<DataReq>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun dataSetChange(){
        notifyDataSetChanged()
    }
}