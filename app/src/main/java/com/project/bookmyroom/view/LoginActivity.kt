package com.project.bookmyroom.view

import android.os.Bundle
import android.view.View
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
            binding.main2.visibility= View.GONE
            viewModel.onLoginButtonClick(this@LoginActivity)
        }

        binding.btnSignUp.setOnClickListener {
            binding.main2.visibility= View.GONE
            binding.textViewSignUp.setText("Already signed up?")
            binding.btnSignUp.setText("Sign in")
            viewModel.onSignUpButtonClick(this@LoginActivity)
        }

        if (binding.btnSignUp.text=="Sign in"){
            binding.btnSignUp.setOnClickListener {
                binding.main2.visibility= View.GONE
                viewModel.onLoginButtonClick(this@LoginActivity)
            }

        }
    }

    override fun onBackPressed() {
        if (binding.main2.visibility == View.GONE) {
            // If main2 layout is hidden, show it and return
            binding.main2.visibility = View.VISIBLE
            return
        }
        super.onBackPressed()
    }
}
