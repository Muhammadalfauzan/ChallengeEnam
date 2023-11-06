package com.example.challengeempat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.challengeempat.databinding.ActivityRegisterBinding
import com.example.challengeempat.util.Result
import com.example.challengeempat.viewmodelregister.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: UserViewModel by viewModels()
    private var registrationSuccessful = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

            }
        }

        binding.tvLogin.setOnClickListener {
            // Tambahkan logika untuk kembali ke LoginActivity jika registrasi berhasil
            if (registrationSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Lakukan registrasi terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    // Setel status registrasi menjadi true
                    registrationSuccessful = true

                }
                is Result.Error -> {
                    Toast.makeText(this, "Registrasi gagal: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
