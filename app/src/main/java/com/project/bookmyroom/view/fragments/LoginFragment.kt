package com.project.bookmyroom.view.fragments

import android.content.Intent
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
import com.project.bookmyroom.model.data.LoginRequest
import com.project.bookmyroom.model.data.LoginResponse
import com.project.bookmyroom.model.data.User
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.CommonDataArea.Companion.userEmail
import com.project.bookmyroom.view.CommonDataArea.Companion.userId
import com.project.bookmyroom.view.CommonDataArea.Companion.userName
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.components.ProgressDialogHandler
import com.project.bookmyroom.view.fragments.ui.login.LoggedInUserView
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModel
import com.project.bookmyroom.view.fragments.ui.login.LoginViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loginButton: Button
    private lateinit var  passwordEditText: EditText
    private lateinit var  usernameEditText: EditText
    private lateinit var progressDialogHandler: ProgressDialogHandler

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
        preferenceManager = PreferenceManager(requireContext())
        progressDialogHandler  = ProgressDialogHandler(requireContext())

        usernameEditText = binding.username
        passwordEditText = binding.password
        loginButton = binding.signIn
        loadingProgressBar = binding.loading
        rememberMeCheckbox = binding.rememberMe



       /* // Restore saved username and password
        usernameEditText.setText(sharedPreferences.getString(KEY_USERNAME, ""))
        passwordEditText.setText(sharedPreferences.getString(KEY_PASSWORD, ""))*/


      /*  if (rememberMeCheckbox.isChecked &&
            usernameEditText.text.isNotBlank() &&
            passwordEditText.text.isNotBlank()
        ) {
            // Perform auto-login
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
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
            progressDialogHandler.showProgressDialog("Entering to your Account...")
           /* loginViewModel.login(
                usernameEditText.text.toString().trim(),
                passwordEditText.text.toString().trim()
            )*/
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty()||  password.isNotEmpty()) {
                val loginRequest = LoginRequest(
                    username ,
                    password

                )
              loginUser(loginRequest)
            } else {
                progressDialogHandler.dismissProgressDialog()

               showToast("Please fill in all fields")
            }
        }
    }
    private fun loginUser(loginRequest: LoginRequest){
        val apiService = RetrofitClient.instance
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ){
                if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Handle successful registration response
                    val message = loginResponse.message
                    val newUser = loginResponse.user
                    if (newUser != null) {
                        // Handle the user data as needed

                        val user = User(
                            newUser._id,
                            newUser.email,
                            loginRequest.password,
                            newUser.firstName,
                            newUser.phone,
                            newUser.userId,
                            newUser.createdAt,
                            newUser.updatedAt,
                            newUser.__v
                        )
                        preferenceManager.saveUser(user)

                        loginViewModel.login(user.firstName,user.password)
                        //showToast("Login Successful: $message")
                    } else {
                        showToast("Login Failed: User data missing ")
                    }
                } else {
                    showToast("Login Failed: Response body is empty")
                }
            } else {
                // Handle unsuccessful registration response
                val errorMessage = response.errorBody()?.string()
                showToast("Login Failed: $errorMessage")
            }
        }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            // Handle network or API call failure
            showToast("Login Failed: ${t.message}")
        }
    })
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
                           preferenceManager.getUser()?.firstName!!,
                            passwordEditText.text.toString(),

                            )
                    } else {
                        // Clear saved credentials if "Remember Me" is not checked
                        preferenceManager.clearCredentials()
                    }
                }
            })    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome_back) + model.displayName
        // TODO : initiate successful logged in experience
        showToast(welcome)        // Finish the current activity
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
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        progressDialogHandler.dismissProgressDialog()
    }

}
