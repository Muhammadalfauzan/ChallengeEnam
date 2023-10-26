package com.example.challengeempat.viewmodelregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.modeluser.User
import com.example.challengeempat.sharedpref.SharedPreffUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                    userRepository.saveUserDataToFirestore(username, noTelepon, email)

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

    suspend fun editUserProfile(
        email: String,
        newUsername: String,
        newNoTelepon: String,
        newPassword: String?
    ): Boolean {
        // Validasi password lama
        val isOldPasswordValid = validateOldPassword(email, "PASSWORD_LAMA")
        if (isOldPasswordValid) {
            val updatedUser = User(newUsername, newNoTelepon, email)

            // Update profil pengguna di Firestore
            val isProfileUpdated = withContext(Dispatchers.IO) {
                userRepository.updateUserDataInFirestore(email, updatedUser)
                true // Profil berhasil diperbarui
            }

            // Update kata sandi jika newPassword tidak null
            val isPasswordUpdated = if (!newPassword.isNullOrEmpty()) {
                val isPasswordChangeSuccessful = changePassword(email, newPassword)
                isPasswordChangeSuccessful
            } else {
                true // Kata sandi tidak diubah, maka dianggap berhasil
            }

            // Kembalikan true jika profil dan kata sandi berhasil diperbarui
            return isProfileUpdated && isPasswordUpdated
        } else {
            // Password lama tidak valid, lakukan penanganan kesalahan
            return false
        }
    }

    private fun changePassword(email: String, newPassword: String): Boolean {
        return try {
            // Menggunakan Firebase Authentication untuk mengganti kata sandi
            val user = FirebaseAuth.getInstance().currentUser
            user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kata sandi berhasil diubah
                } else {
                    // Terjadi kesalahan saat mengubah kata sandi
                }
            }
            true // Pengembalian true disini tidak diperlukan
        } catch (e: Exception) {
            // Terjadi kesalahan saat mengubah kata sandi
            false
        }
    }

    private fun validateOldPassword(email: String, oldPassword: String): Boolean {

        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        return try {
            user?.reauthenticate(credential) != null
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}

