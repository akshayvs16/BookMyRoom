package com.project.bookmyroom.viewmodel

import androidx.lifecycle.ViewModel
import com.project.bookmyroom.R
import com.project.bookmyroom.view.activity.LoginActivity
import com.project.bookmyroom.view.fragments.SignUpFragment
import com.project.bookmyroom.view.fragments.ui.login.LoginFragment
import com.project.bookmyroom.view.replaceFragment


class LoginViewModel : ViewModel() {


    fun onSignUpButtonClick(loginActivity: LoginActivity) {
        // Perform Sign In action here
        loginActivity.replaceFragment(R.id.frame_Layout_container, SignUpFragment())
    }

    fun onLoginButtonClick(loginActivity: LoginActivity) {
        loginActivity.replaceFragment(R.id.frame_Layout_container, LoginFragment())
    }

}
