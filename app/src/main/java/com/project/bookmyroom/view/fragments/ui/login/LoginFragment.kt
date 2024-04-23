package com.project.bookmyroom.view.fragments.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.FragmentLoginBinding
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.activity.MainActivity


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loginButton: Button
    private lateinit var  passwordEditText: EditText
    private lateinit var  usernameEditText: EditText

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER_ME = "remember_me"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnBack = binding.btnBack
        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        preferenceManager = PreferenceManager(requireContext())

        usernameEditText = binding.username
        passwordEditText = binding.password
        loginButton = binding.signIn
        loadingProgressBar = binding.loading
        rememberMeCheckbox = binding.rememberMe



       /* // Restore saved username and password
        usernameEditText.setText(sharedPreferences.getString(KEY_USERNAME, ""))
        passwordEditText.setText(sharedPreferences.getString(KEY_PASSWORD, ""))*/


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

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)



        rememberMeCheckbox.isChecked = preferenceManager.areCredentialsSaved()
        if (rememberMeCheckbox.isChecked) {
            usernameEditText.setText(preferenceManager.getUsername() ?: "")
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
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun observeModel() {
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
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
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                    if (rememberMeCheckbox.isChecked) {
                        preferenceManager.saveCredentials(
                            usernameEditText.text.toString(),
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

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
