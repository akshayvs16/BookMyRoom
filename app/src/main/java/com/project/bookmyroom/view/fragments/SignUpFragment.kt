package com.project.bookmyroom.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.FragmentLoginBinding
import com.project.bookmyroom.databinding.FragmentSignUpBinding
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.fragments.ui.login.LoggedInUserView
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModel
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModelFactory

class SignUpFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var loginViewModel: LoginViewModel

    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnBack = binding.btnBack
        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        preferenceManager = PreferenceManager(requireContext())
        usernameEditText = binding.username
        emailEditText = binding.userEmailInput
        passwordEditText = binding.password
        signUpButton = binding.signUp
        rememberMeCheckbox = binding.rememberMe



        if (rememberMeCheckbox.isChecked &&
            usernameEditText.text.isNotBlank() &&
            passwordEditText.text.isNotBlank()
        ) {
            // Perform auto-login
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }


       /* signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save credentials to preference manager
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                // Optionally, you can finish this fragment to prevent going back to it using the back button
            }
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        rememberMeCheckbox.isChecked = preferenceManager.areCredentialsSaved()
        if (rememberMeCheckbox.isChecked) {
            usernameEditText.setText(preferenceManager.getUsername() ?: "")
            emailEditText.setText(preferenceManager.getEmail() ?: "")
            passwordEditText.setText(preferenceManager.getPassword() ?: "")
        }
        observeModel()


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.signup(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString(),
                    emailEditText.text.toString()
                )
            }
            false
        }
        signUpButton.setOnClickListener {
            loginViewModel.signup(
                usernameEditText.text.toString(),
                passwordEditText.text.toString(),
                emailEditText.text.toString()
            )
        }
    }

    private fun observeModel() {
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                signUpButton.isEnabled = true

                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loginResult.error?.let {
/*
                    showLoginFailed(it)
*/
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                    if (rememberMeCheckbox.isChecked) {
                        preferenceManager.saveCredentials(
                            usernameEditText.text.toString(),
                            emailEditText.text.toString(),
                            passwordEditText.text.toString(),
                        )
                    } else {
                        // Clear saved credentials if "Remember Me" is not checked
                        preferenceManager.clearCredentials()
                    }
                }
            })    }
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        // Finish the current activity
        requireActivity().finish()

        // Start MainActivity
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
