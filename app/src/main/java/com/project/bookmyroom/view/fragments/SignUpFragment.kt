package com.project.bookmyroom.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.project.bookmyroom.R
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.activity.MainActivity

class SignUpFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        preferenceManager = PreferenceManager(requireContext())
        usernameEditText = view.findViewById(R.id.username)
        emailEditText = view.findViewById(R.id.user_email_input)
        passwordEditText = view.findViewById(R.id.password)
        signUpButton = view.findViewById(R.id.sign_up)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save credentials to preference manager
                preferenceManager.saveCredentials(username, email, password)
                requireActivity().finish()

                // Start MainActivity
                startActivity(Intent(requireContext(), MainActivity::class.java))

                // Optionally, you can finish this fragment to prevent going back to it using the back button
            }
        }

        return view
    }
}
