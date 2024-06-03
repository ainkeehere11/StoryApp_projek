package com.dicoding.submissionintermediate.story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediate.R
import com.dicoding.submissionintermediate.databinding.ActivityDetailBinding


@Suppress("DEPRECATION")
class Detail : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val NAME = "name"
        const val DESC = "desc"
        const val PHOTO = "photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME)
        val description = intent.getStringExtra(DESC)
        val photoUrl = intent.getStringExtra(PHOTO)
        binding.apply {
            tvName.text = name
            tvDescription.text = description
        }

        Glide.with(this)
            .load(photoUrl)
            .into(binding.imgPhoto)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}