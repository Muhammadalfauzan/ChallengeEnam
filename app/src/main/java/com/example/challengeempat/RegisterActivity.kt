package com.example.challengeempat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.challengeempat.databinding.ActivityRegisterBinding
import com.example.challengeempat.viewmodelregister.UserViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel menggunakan ViewModelProvider
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Inisialisasi sharedPrefUser
        val sharedPrefUser = SharedPreffUser(this)
        viewModel.setUserPreferences(sharedPrefUser)

        viewModel.registrationMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        binding.btnRegistrasi.setOnClickListener {
            if (validateInput()) {
                val usernameText = binding.etUsernameRegis.text.toString()
                val noTeleponText = binding.etNoTelepon.text.toString()
                val emailText = binding.etEmailRegis.text.toString()
                val password = binding.etPasswordRegis.text.toString()

                viewModel.register(usernameText, noTeleponText, emailText, password)

                // Registrasi berhasil, munculkan pesan Toast
                viewModel.registrationMessage.observe(this) { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(): Boolean {
        val usernameText = binding.etUsernameRegis.text.toString()
        val noTeleponText = binding.etNoTelepon.text.toString()
        val emailText = binding.etEmailRegis.text.toString()
        val password = binding.etPasswordRegis.text.toString()
        val confirmPassword = binding.etPasswordConfirm.text.toString()

        if (usernameText.isEmpty() || noTeleponText.isEmpty() || emailText.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            if (usernameText.isEmpty()) {
                binding.etUsernameRegis.error = "Enter your name"
            }
            if (noTeleponText.isEmpty()) {
                binding.etNoTelepon.error = "Enter your phone number"
            }
            if (emailText.isEmpty()) {
                binding.etEmailRegis.error = "Enter your email"
            }
            if (password.isEmpty()) {
                binding.etPasswordRegis.error = "Enter your password"
            }
            if (confirmPassword.isEmpty()) {
                binding.etPasswordConfirm.error = "Re-enter your password"
            }

            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validasi password dengan confirmPassword
        if (password != confirmPassword) {
            binding.etPasswordConfirm.error = "Passwords do not match"
            return false
        }

        return true
    }

}
