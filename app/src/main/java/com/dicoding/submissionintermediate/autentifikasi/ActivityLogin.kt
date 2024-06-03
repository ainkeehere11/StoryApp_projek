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
import com.dicoding.submissionintermediate.MainActivity
import com.dicoding.submissionintermediate.R
import com.dicoding.submissionintermediate.databinding.ActivityLoginBinding
import com.dicoding.submissionintermediate.respon.LoginResult
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.FactoryViewModel
import com.dicoding.submissionintermediate.viewmodel.ModelUser

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ActivityLogin : AppCompatActivity() {

    private lateinit var pref: AutentifikasiPref
    private val loginViewModel: ViewModelAuth by viewModels {
        FactoryViewModel(pref, this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: ModelUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        pref = AutentifikasiPref.getInstance(dataStore)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        setContentView(binding.root)
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

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailEditText.error = getString(R.string.email_input)
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = getString(R.string.password_input)
                } password.length < 8 -> {
                Toast.makeText(this, getString(R.string.must_8_character), Toast.LENGTH_SHORT).show()
            }
                else -> {
                    loginViewModel.loginUser(email, password)
                    loginViewModel.loginUser.observe(this) {result ->
                        val name = result.name
                        val userId = result.userId
                        val token = result.token

                        loginViewModel.saveUser(LoginResult(name, userId, token, true))
                        loginViewModel.login()
                    }
                    loginViewModel.loginStatus.observe(this) { success ->
                        if (success) {
                            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
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
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            startDelay = 500
        }.start()
    }
}