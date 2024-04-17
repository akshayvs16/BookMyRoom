package com.project.bookmyroom.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.ActivityLoginBinding
import com.project.bookmyroom.view.fragments.ui.login.LoginFragment
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


        binding.btnLogin.setOnClickListener {
            viewModel.onLoginButtonClick(this@LoginActivity)
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.onSignUpButtonClick(this@LoginActivity)
        }
    }
}
