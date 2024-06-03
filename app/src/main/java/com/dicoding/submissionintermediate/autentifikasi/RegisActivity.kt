package com.dicoding.submissionintermediate.autentifikasi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
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
import com.dicoding.submissionintermediate.R
import com.dicoding.submissionintermediate.databinding.ActivityRegisBinding
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.FactoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RegisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisBinding
    private lateinit var pref: AutentifikasiPref
    private val viewModel: ViewModelAuth by viewModels {
        FactoryViewModel(pref, this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityRegisBinding.inflate(layoutInflater)

        pref = AutentifikasiPref.getInstance(dataStore)

        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        supportActionBar?.hide()
    }

    private fun setupViewModel() {

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isBlank() -> {
                    binding.edName.error = getString(R.string.input_name)
                }
                email.isBlank() -> {
                    binding.edEmail.error = getString(R.string.email_input)
                }
                password.isBlank() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.password_input)
                }
                password.length < 8 -> {
                    Toast.makeText(this, getString(R.string.must_8_character), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.register(name, email, password)
                    viewModel.registerUser.observe(this) {success ->
                        if (success.error) {
                            Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ActivityLogin::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, success.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.image, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.edName, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(
            binding.edEmail,
            View.ALPHA,
            1f
        ).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.edPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(
            binding.passwordEditTextLayout,
            View.ALPHA,
            1f
        ).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 500
        }.start()
    }
}