package com.example.challengeempat.viewmodelregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengeempat.modeluser.User
import com.example.challengeempat.sharedpref.SharedPreffUser
import com.example.challengeempat.util.Result
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreffUser: SharedPreffUser
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> = _registerResult

    val registrationMessage = MutableLiveData<String>()
    val userLiveData = MutableLiveData<User?>()
    fun isLoggedIn(): Boolean {
        return sharedPreffUser.isLoggedIn()
    }

    fun setLoggedIn(value: Boolean) {
        sharedPreffUser.setLoggedIn(value)
    }

    suspend fun getUserFromFirestore(email: String): User? {
        return userRepository.getUserDocument(email)
    }


    fun fetchUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser?.email
            if (email != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    val user = userRepository.getUserDocument(email)
                    withContext(Dispatchers.Main) {
                        userLiveData.value = user
                    }
                }
            }
        }
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
                    User(username, noTelepon, email)
                    userRepository.saveUserDataToFirestore(username, noTelepon, email)
                    registrationMessage.value = "Registrasi berhasil"
                    _registerResult.value = Result.Success(Unit)
                }
            }
            .addOnFailureListener { exception ->
                registrationMessage.value = "Registrasi gagal: ${exception.message}"
                _registerResult.value = Result.Error(exception)
            }
    }

    fun login(email: String, password: String): LiveData<Result<FirebaseUser?>> {
        val result = MutableLiveData<Result<FirebaseUser?>>()

        if (sharedPreffUser.isLoggedIn()) {
            result.value = Result.Success(auth.currentUser)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPreffUser.setLoggedIn(true)
                        result.value = Result.Success(auth.currentUser)
                    } else {
                        val loginFailure = Exception("Login failed")
                        result.value = Result.Error(loginFailure)
                    }
                }
                .addOnFailureListener { exception ->
                    result.value = Result.Error(exception)
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
        val isOldPasswordValid = validateOldPassword(email, "PASSWORD_LAMA")
        if (isOldPasswordValid) {
            val updatedUser = User(newUsername, newNoTelepon, email)
            val isProfileUpdated = withContext(Dispatchers.IO) {
                userRepository.updateUserDataInFirestore(email, updatedUser)
                true
            }
            val isPasswordUpdated = if (!newPassword.isNullOrEmpty()) {
                val isPasswordChangeSuccessful = changePassword(newPassword)
                isPasswordChangeSuccessful
            } else {
                true
            }

            return isProfileUpdated && isPasswordUpdated
        } else {
            return false
        }
    }

    private fun changePassword(newPassword: String): Boolean {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password berhasil diubah
                } else {
                    // Gagal mengubah password
                }
            }
            true
        } catch (e: Exception) {
            // Exception saat mengubah password
            false
        }
    }

    private fun validateOldPassword(email: String, oldPassword: String): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        return try {
            user?.reauthenticate(credential) != null
        } catch (e: Exception) {
            // Exception saat melakukan reauthentication
            false
        }
    }
}