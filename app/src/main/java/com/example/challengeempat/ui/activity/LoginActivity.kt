package com.example.challengeempat.ui.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.challengeempat.R
import com.example.challengeempat.sharedpref.SharedPreffUser
import com.example.challengeempat.modeluser.User
import com.example.challengeempat.viewmodelregister.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView
    private lateinit var progress: ProgressDialog
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPrefUser: SharedPreffUser
    private lateinit var currentUser: FirebaseUser

    private lateinit var loginViewModel: UserViewModel

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        loginViewModel.setUserPreferences(SharedPreffUser(this)) // Menginisialisasi SharedPreffUser

        editEmail = findViewById(R.id.et_email)
        editPassword = findViewById(R.id.et_password)
        btnRegister = findViewById(R.id.tv_Registrasi)
        btnLogin = findViewById(R.id.btn_login)

        progress = ProgressDialog(this)
        progress.setTitle("Logging")
        progress.setMessage("Silahkan tunggu")

        sharedPrefUser = SharedPreffUser(this)

        if (sharedPrefUser.isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            btnLogin.setOnClickListener {
                if (editEmail.text!!.isNotEmpty() && editPassword.text!!.isNotEmpty()) {
                    prosesLogin()
                } else {
                    Toast.makeText(
                        this,
                        "Silahkan Isi email dan password terlebih dahulu",
                        LENGTH_SHORT
                    ).show()
                }
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun prosesLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progress.show()
        loginViewModel.login(email, password).observe(this, Observer { result ->
            when {
                result.isSuccess -> {
                    val user = result.getOrNull()
                    if (user != null) {
                        currentUser = user

                        // Mengambil data pengguna dari Firestore
                        val firestore = FirebaseFirestore.getInstance()
                        val userCollection = firestore.collection("users")
                        userCollection.document(user.email!!).get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val userData = documentSnapshot.toObject(User::class.java)
                                    if (userData != null) {
                                        // Di sini Anda dapat menggunakan data pengguna dari Firestore
                                        val username = userData.username
                                        userData.noTelepon

                                        // Selanjutnya, Anda dapat menggunakannya sesuai kebutuhan.
                                    }
                                }

                                // Navigasi ke layar beranda atau lainnya setelah login berhasil
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                progress.dismiss()
                                finish()
                            }
                            .addOnFailureListener { _ ->
                                Toast.makeText(this, "Gagal mengambil data pengguna dari Firestore", LENGTH_SHORT).show()
                                progress.dismiss()
                            }
                    } else {
                        Toast.makeText(this, "Akun Tidak terdaftar silahkan registrasi dulu", LENGTH_SHORT).show()
                        progress.dismiss()
                    }
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()?.message
                    Toast.makeText(this, "Login gagal: $error", LENGTH_SHORT).show()
                    progress.dismiss()
                }
                else -> {
                }
            }
        })
    }
}
