package com.project.bookmyroom.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.bookmyroom.databinding.FragmentProfileBinding
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.activity.DistrictActivity
import com.project.bookmyroom.view.activity.LoginActivity
import com.project.bookmyroom.view.components.ExitDialog

class ProfileFragment : Fragment() {
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUsername()
        binding.logoutCard.setOnClickListener {
            onLogoutClicked()
        }

        binding.districtChoose.setOnClickListener {
            val i = Intent(context, DistrictActivity::class.java)
            requireContext().startActivity(i)
        }
    }

    private fun displayUsername() {
        val username = preferenceManager.getUser()?.firstName
        val useremail = preferenceManager.getUser()?.email
        val userphone = preferenceManager.getUser()?.phone
            binding.userName.text ="Name: ${ username}"
            binding.userEmail.text ="Email: ${ useremail}"
            binding.userPhone.text ="Phone: ${ userphone}"

    }

    fun onLogoutClicked() {
        showExitConfirmationDialog()

    }
    private fun showExitConfirmationDialog() {
        val exitDialog = ExitDialog(requireContext())
        exitDialog.showExitDialog {
            // Handle logout action here, such as clearing user data, navigating to login screen, etc.
            // For example:
            preferenceManager.clearUser()
            preferenceManager.clearCredentials()
            navigateToLoginScreen()
        }
        
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}
