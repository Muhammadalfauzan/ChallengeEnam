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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengeempat.R
import com.example.challengeempat.ViewModelFactory
import com.example.challengeempat.databinding.FragmentProfileBinding
import com.example.challengeempat.sharedpref.SharedPreffUser
import com.example.challengeempat.ui.activity.LoginActivity
import com.example.challengeempat.viewmodel.CartViewModel
import com.example.challengeempat.viewmodelregister.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefUser: SharedPreffUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpCartViewModel()
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        sharedPrefUser = SharedPreffUser(requireContext())

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
                sharedPrefUser.setLoggedIn(false)

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

    private fun setUpCartViewModel() {
        val viewModelFactory = ViewModelFactory(requireActivity().application)
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }
}
