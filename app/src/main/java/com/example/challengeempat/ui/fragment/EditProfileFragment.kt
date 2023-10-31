package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengeempat.databinding.FragmentEditProfileBinding
import com.example.challengeempat.viewmodelregister.UserViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        currentUser = FirebaseAuth.getInstance().currentUser ?: return view

        // Mendapatkan data pengguna dari Firestore dan mengisi  edit profil
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val user = currentUser.email?.let { userViewModel.getUserFromFirestore(it) }
            if (user != null) {
                withContext(Dispatchers.Main) {
                    binding.etEditUsername.setText(user.username)
                    binding.etEditNoTelephone.setText(user.noTelepon)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val username = binding.etEditUsername.text.toString()
            val passwordLama = binding.etEditPasswordLama.text.toString()
            val passwordBaru = binding.etEditPassword.text.toString()
            val noTelepon = binding.etEditNoTelephone.text.toString()

            if (username.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    val isPasswordValid = if (passwordLama.isNotEmpty()) {
                        try {
                            // Reauthenticate to ensure the old password is correct
                            val credential = EmailAuthProvider.getCredential(currentUser!!.email!!, passwordLama)
                            currentUser.reauthenticate(credential).await()
                            true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            false
                        }
                    } else {
                        false // Password lama wajib diisi jika mengganti username
                    }

                    when {
                        isPasswordValid -> {
                            if (passwordBaru.isEmpty() || passwordBaru.length >= 6) {
                                val isProfileUpdated = userViewModel.editUserProfile(
                                    currentUser!!.email!!,
                                    username,
                                    noTelepon,
                                    if (passwordBaru.isNotEmpty()) passwordBaru else null
                                )

                                if (isProfileUpdated) {
                                    // Data berhasil diubah
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Profil berhasil diperbarui",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navigasi kembali ke halaman profil (ProfileFragment)
                                        findNavController().popBackStack()
                                    }
                                } else {
                                    // Data tidak berhasil diubah
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Gagal memperbarui profil",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                // Password baru kurang dari 6 karakter
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Password baru harus memiliki minimal 6 karakter",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        else -> {
                            // Password lama tidak valid
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Password lama tidak valid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                // Username kosong
                Toast.makeText(
                    requireContext(),
                    "Username tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack() // Navigasi kembali ke halaman sebelumnya (ProfileFragment)
        }

        return view
    }
}
