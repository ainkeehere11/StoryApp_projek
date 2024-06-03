package com.dicoding.submissionintermediate.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediate.databinding.ItemStoryBinding
import com.dicoding.submissionintermediate.viewmodel.Story

class StoryAdapter(private val listStories: List<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: Story) {
            binding.tvName.text = listStory.name
            Glide.with(itemView.context)
                .load(listStory.photoUrl)
                .into(binding.imageView2)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Detail::class.java)
                intent.putExtra(Detail.NAME, listStory.name)
                intent.putExtra(Detail.PHOTO, listStory.photoUrl)
                intent.putExtra(Detail.DESC, listStory.description)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listStories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }
}