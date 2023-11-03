@file:Suppress("DEPRECATION")

package com.example.challengeempat.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengeempat.R
import com.example.challengeempat.databinding.FragmentProfileBinding
import com.example.challengeempat.ui.activity.LoginActivity
import com.example.challengeempat.viewmodelregister.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Mengambil data pengguna dari ViewModel (sudah login) dan menampilkannya di profil
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val user = userViewModel.getUserFromFirestore(email)
                    if (user != null) {
                        withContext(Dispatchers.Main) {
                            binding.txtUsername.text = user.username
                            binding.txtEmail.text = user.email
                            binding.txtNotlp.text = user.noTelepon
                        }
                    }
                }
            }
        }

        binding.btnLogut.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah Anda yakin ingin keluar?")
            builder.setPositiveButton("Ya") { _, _ ->
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Logout")
                progressDialog.setMessage("Sedang proses logout...")
                progressDialog.show()

                FirebaseAuth.getInstance().signOut()
                userViewModel.setLoggedIn(false) // Perbaikan di sini, menggunakan sharedPreffUser dari UserViewModel

                progressDialog.dismiss()

                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Logout gagal", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        return view
    }
}
