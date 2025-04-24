package com.example.myassignment.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T, VB : ViewBinding>(
    private val itemList: List<T>,
    private val layoutInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val onBind: (VB, T, Int) -> Unit
) : RecyclerView.Adapter<GenericAdapter.GenericViewHolder<T, VB>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T, VB> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = layoutInflater.invoke(inflater, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T, VB>, position: Int) {
        onBind.invoke(holder.binding, itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    class GenericViewHolder<T, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
