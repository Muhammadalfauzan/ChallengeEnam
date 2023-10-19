@file:Suppress("DEPRECATION")

package com.example.challengeempat

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.example.challengeempat.ui.activity.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView
    private lateinit var progress: ProgressDialog
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPrefUser: SharedPreffUser
    private lateinit var currentUser: FirebaseUser

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.et_email)
        editPassword = findViewById(R.id.et_password)
        btnRegister = findViewById(R.id.tv_Registrasi)
        btnLogin = findViewById(R.id.btn_login)

        progress = ProgressDialog(this)
        progress.setTitle("Logging")
        progress.setMessage("Silahkan tunggu")

        sharedPrefUser = SharedPreffUser(this)

        if (sharedPrefUser.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnLogin.setOnClickListener {
            if (editEmail.text!!.isNotEmpty() && editPassword.text!!.isNotEmpty()) {
                prosesLogin()
            } else {
                Toast.makeText(this, "Silahkan Isi email dan password terlebih dahulu", LENGTH_SHORT).show()
            }
        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun prosesLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progress.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                currentUser = authResult.user!!

                sharedPrefUser.setLoggedIn(true)

                val user = authResult.user
                val username = user?.displayName ?: ""
                val noTelepon = user?.phoneNumber ?: ""
                val email = user?.email ?: ""

                sharedPrefUser.saveUserData(username, noTelepon.toString(), email)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                progress.dismiss()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Akun Tidak terdaftar silahkan registrasi dulu", LENGTH_SHORT).show()
                progress.dismiss()
            }
    }

}
