package com.example.challengeempat.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.challengeempat.LoginActivity
import com.example.challengeempat.R
import com.example.challengeempat.SharedPreffUser


class ProfileFragment : Fragment() {
    private lateinit var sharedPreffUser: SharedPreffUser
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var noTeleponTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreffUser = SharedPreffUser(requireContext())
        usernameTextView = view.findViewById(R.id.txt_username)
        emailTextView = view.findViewById(R.id.txt_email)
        noTeleponTextView = view.findViewById(R.id.txt_notlp)
        logoutButton = view.findViewById(R.id.btn_logut)

        val username = sharedPreffUser.getUsername()
        val email = sharedPreffUser.getEmail()
        val noTelepon = sharedPreffUser.getNoTelepon()

        usernameTextView.text = username
        emailTextView.text = email
        noTeleponTextView.text = noTelepon

        logoutButton.setOnClickListener {
            sharedPreffUser.setLoggedIn(false)
            sharedPreffUser.clearUserData()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
