package com.dicoding.submissionintermediate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionintermediate.autentifikasi.ActivityLogin
import com.dicoding.submissionintermediate.databinding.ActivityMainBinding
import com.dicoding.submissionintermediate.maps.ActivityMaps
import com.dicoding.submissionintermediate.story.AddStory
import com.dicoding.submissionintermediate.story.StoryAdapter
import com.dicoding.submissionintermediate.story.ViewModelStory
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.FactoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var pref: AutentifikasiPref
    private val viewModel: ViewModelMain by viewModels {
        FactoryViewModel(pref, this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = AutentifikasiPref.getInstance(dataStore)

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
        }

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin){
                true
            } else {
                startActivity(Intent(this, ActivityLogin::class.java))
                finish()
            }
        }

        getStoryData()

        setContentView(binding.root)
    }

    private fun getStoryData() {
        adapter = MainAdapter()
        binding.stories.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
        val layoutManager = LinearLayoutManager(this)
        binding.stories.layoutManager = layoutManager
        viewModel.story.observe(this) {
            adapter.submitData(this.lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_option,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()
                Toast.makeText(this, "Success logout", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ActivityLogin::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.menu_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                return true
            }
            R.id.maps -> {
                val intent = Intent(this, ActivityMaps::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}