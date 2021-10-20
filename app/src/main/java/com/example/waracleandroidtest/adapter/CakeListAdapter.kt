package com.example.waracleandroidtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waracleandroidtest.domain.entity.Cake

class CakeListAdapter : ListAdapter<Cake, CakeListAdapter.ItemsViewHolder>(DiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder = ItemsViewHolder(
        CakeItemViewBinding.inflate(
            LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val cake = getItem(position)
        holder.bind(cake)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Cake>() {
        override fun areItemsTheSame(oldItem: Cake, newItem: Cake): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Cake, newItem: Cake): Boolean {
            return oldItem.title == newItem.title
        }
    }

    inner class ItemsViewHolder(private val binding: CakeItemViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(cake: Cake) {
            binding.cakeTitle.text = cake.title
            binding.cakeDesc.text = cake.desc
            Glide.with(itemView.context)
                .load(cake.image)
                .into(binding.cakeImage)
        }
    }

}