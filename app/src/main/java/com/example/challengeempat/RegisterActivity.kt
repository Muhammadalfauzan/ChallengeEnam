@file:Suppress("DEPRECATION")

package com.example.challengeempat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var noTelepon: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordConf: EditText
    private lateinit var btnRegistrasi: Button
    private lateinit var btnLogin: TextView
    private lateinit var progres: ProgressDialog

    lateinit var sharedPreffUser: SharedPreffUser
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.et_usernameRegis)
        noTelepon = findViewById(R.id.et_noTelepon)
        editEmail = findViewById(R.id.et_emailRegis)
        editPassword = findViewById(R.id.et_passwordRegis)
        editPasswordConf = findViewById(R.id.et_passwordConfirm)
        btnRegistrasi = findViewById(R.id.btn_registrasi)
        btnLogin = findViewById(R.id.tv_Login)// Button Registrasi

        sharedPreffUser = SharedPreffUser(this)
        progres = ProgressDialog(this)
        progres.setTitle("Logging")
        progres.setMessage("Silahkan tunggu")

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegistrasi.setOnClickListener {
            if (validateInput()) {
                processRegister()
            }
        }
    }


    private fun validateInput(): Boolean {
        val usernameText = username.text.toString()
        val noTeleponText = noTelepon.text.toString()
        val emailText = editEmail.text.toString()
        val password = editPassword.text.toString()
        val confirmPassword = editPasswordConf.text.toString()

        if (usernameText.isEmpty() || noTeleponText.isEmpty() || emailText.isEmpty() || password.isEmpty()) {
            showToast("Silahkan isi dulu semua data!")
            return false
        }

        if (password != confirmPassword) {
            showToast("Konfirmasi kata sandi harus sama!")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    private fun processRegister() {
        val usernameText = username.text.toString()
        val noTeleponText = noTelepon.text.toString()
        val emailText = editEmail.text.toString()
        val password = editPassword.text.toString()

        progres.show()
        firebaseAuth.createUserWithEmailAndPassword(emailText, password)
            .addOnCompleteListener { authTask ->
                progres.dismiss()
                if (authTask.isSuccessful) {
                    sharedPreffUser.saveUserData(usernameText, noTeleponText, emailText)

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Registrasi gagal, coba lagi.")
                }
            }
            .addOnFailureListener { error ->
                error.localizedMessage?.let { showToast(it) }
            }
    }
}