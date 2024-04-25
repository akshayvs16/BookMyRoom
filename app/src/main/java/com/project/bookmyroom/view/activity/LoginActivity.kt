package com.project.bookmyroom.view.activity

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.ActivityLoginBinding
import com.project.bookmyroom.view.fragments.SignUpFragment
import com.project.bookmyroom.view.fragments.LoginFragment
import com.project.bookmyroom.view.replaceFragment

class LoginActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: ActivityLoginBinding
   // private val viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        /*binding.viewModel = viewModel
        binding.lifecycleOwner = this*/
        val group = findViewById<RadioGroup>(R.id.radio_group)
        group.setOnCheckedChangeListener(this)
        replaceFragment(R.id.frame_Layout_container, LoginFragment())
        // Bind click listeners






    }

    override fun onBackPressed() {
        if (binding.main2.visibility == View.GONE) {
            // If main2 layout is hidden, show it and return
            binding.main2.visibility = View.VISIBLE
            return
        }
        super.onBackPressed()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val checkRadioButton = group?.findViewById<RadioButton>(group.checkedRadioButtonId)

        checkRadioButton?.let {

            when (checkRadioButton.id) {
                R.id.login_radio_button -> {
                    replaceFragment(R.id.frame_Layout_container, LoginFragment())
                }
                R.id.signup_radio_button ->{
                    replaceFragment(R.id.frame_Layout_container, SignUpFragment())

                }
                else -> {
                }
            }
        }
    }
}
