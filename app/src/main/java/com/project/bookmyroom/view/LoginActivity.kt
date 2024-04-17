package com.project.bookmyroom.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.ActivityLoginBinding
import com.project.bookmyroom.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Bind click listeners
        binding.btnSignIn.setOnClickListener {
            viewModel.onSignInButtonClick()
        }

        binding.btnLogin.setOnClickListener {
            viewModel.onLoginButtonClick()
        }
    }
}
