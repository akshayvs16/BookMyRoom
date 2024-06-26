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
import com.project.bookmyroom.databinding.FragmentSignUpBinding
import com.project.bookmyroom.model.data.RegisterRequest
import com.project.bookmyroom.model.data.RegisterResponse
import com.project.bookmyroom.model.data.User
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.components.ProgressDialogHandler
import com.project.bookmyroom.view.fragments.ui.login.LoggedInUserView
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModel
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressDialogHandler:ProgressDialogHandler
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
        progressDialogHandler  = ProgressDialogHandler(requireContext())

        usernameEditText = binding.username
        emailEditText = binding.userEmailInput
        phoneEditText = binding.userPhoneInput
        passwordEditText = binding.password
        signUpButton = binding.signUp
        rememberMeCheckbox = binding.rememberMe

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
                val username = usernameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                val mobile = phoneEditText.text.toString().trim()

                // Validate name
                if (username.isEmpty()  ) {
                    showToast("Please enter your name")
                    return@setOnClickListener
                }

                // Validate email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showToast("Please enter a valid email address")
                    return@setOnClickListener
                }

                // Validate mobile (add more validation criteria if needed)
                if (mobile.isEmpty() ||mobile.length!=10) {
                    showToast("Please Enter your 10 Digit mobile number")
                    return@setOnClickListener
                }

                // Validate password
                if (password.length < 5) {
                    showToast("Password must be at least 5 characters")
                    return@setOnClickListener
                }
                if (username.isNotEmpty() || email.isNotEmpty() || mobile.isNotEmpty() || password.isNotEmpty()) {


                    progressDialogHandler.showProgressDialog("Creating Account...")

                    if (username.isNotEmpty() || email.isNotEmpty() || mobile.isNotEmpty() || password.isNotEmpty()) {
                        val signUpRequest = RegisterRequest(
                            email = email,
                            password = password,
                            firstName = username,
                            phone = mobile // Assuming you have added phone input
                        )
                        signUpApi(signUpRequest)// Call the API using the ViewModel

                    } else {
                        progressDialogHandler.dismissProgressDialog()

                        showToast("Please fill in all fields")
                    }
                }else {
                    progressDialogHandler.dismissProgressDialog()

                    showToast("Please fill in all fields")
                }
            }
    }

    private fun signUpApi(registerRequest: RegisterRequest) {
        val apiService = RetrofitClient.instance
        val call = apiService.registerUser(registerRequest)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        // Handle successful registration response
                        val message = registerResponse.message
                        val newUser = registerResponse.newUser
                        if (newUser != null) {

                            // Show success message
                            val user = User(
                                newUser._id,
                                newUser.email,
                                newUser.password,
                                newUser.firstName,
                                newUser.phone,
                                newUser.userId,
                                newUser.createdAt,
                                newUser.updatedAt,
                                newUser.__v
                            )
                            preferenceManager.saveUser(user)
                            loginViewModel.signup(user.firstName,user.password,user.email)
                           // showToast("Registration Successful: $message")
                        } else {
                            showToast("Registration Failed: User data missing ")
                        }
                    } else {
                        showToast("Registration Failed: Response body is empty")
                    }
                } else {
                    // Handle unsuccessful registration response
                    val errorMessage = response.errorBody()?.string()
                    showToast("Registration Failed: Try Another Email $errorMessage.")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Handle network or API call failure
                showToast("Registration Failed:Network Failure/Server Error ${t.message}")
            }
        })
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
        showToast(welcome)        // Finish the current activity
        requireActivity().finish()

        // Start MainActivity
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        progressDialogHandler.dismissProgressDialog()
    }

}
