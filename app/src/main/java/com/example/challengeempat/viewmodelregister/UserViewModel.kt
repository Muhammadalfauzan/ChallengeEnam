package com.example.challengeempat.viewmodelregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.SharedPreffUser
import com.example.challengeempat.modeluser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var sharedPrefUser: SharedPreffUser
    private val userRepository = UserRepository()

    fun setUserPreferences(sharedPreffUser: SharedPreffUser) {
        this.sharedPrefUser = sharedPreffUser
    }


    val registrationMessage = MutableLiveData<String>()

    suspend fun getUserFromFirestore(email: String): User? {
        return userRepository.getUserDocument(email)
    }

    fun register(
        username: String,
        noTelepon: String,
        email: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                if (authResult.user != null) {
                    // Pengguna berhasil dibuat di Firebase Authentication
                    val user = User(username, noTelepon, email)

                    // Simpan data pengguna ke Firestore menggunakan UserRepo
                    userRepository.saveUserDataToFirestore(username, noTelepon, email,)

                    registrationMessage.value = "Registrasi berhasil" // Set pesan berhasil
                }
            }
            .addOnFailureListener { exception ->
                registrationMessage.value =
                    "Registrasi gagal: ${exception.message}" // Set pesan gagal
            }
    }


    fun login(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val result = MutableLiveData<Result<FirebaseUser?>>()

        if (sharedPrefUser.isLoggedIn()) {

            result.value = Result.success(auth.currentUser)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPrefUser.setLoggedIn(true) // Set status login menjadi true
                        result.value = Result.success(auth.currentUser)
                    } else {
                        result.value = Result.failure(Exception("Login failed"))
                    }
                }
                .addOnFailureListener { exception ->

                    result.value = Result.failure(exception)
                }
        }

        return result
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}

