package com.dicoding.submissionintermediate

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediate.databinding.ItemStoryBinding
import com.dicoding.submissionintermediate.story.Detail
import com.dicoding.submissionintermediate.viewmodel.Story

class MainAdapter:
    PagingDataAdapter<Story, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            val name = data.name
            val image = data.photoUrl
            binding.tvName.text = name
            Glide.with(binding.imageView2)
                .load(image)
                .into(binding.imageView2)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Detail::class.java)
                intent.putExtra(Detail.NAME, data.name)
                intent.putExtra(Detail.PHOTO, data.photoUrl)
                intent.putExtra(Detail.DESC, data.description)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}