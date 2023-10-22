package com.example.challengeempat.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.challengeempat.LoginActivity
import com.example.challengeempat.R
import com.example.challengeempat.SharedPreffUser
import com.example.challengeempat.ViewModelFactory
import com.example.challengeempat.viewmodel.CartViewModel
import com.example.challengeempat.viewmodelregister.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var noTeleponTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefUser: SharedPreffUser // Tambahkan inisialisasi shared preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        setUpCartViewModel()
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        sharedPrefUser = SharedPreffUser(requireContext()) // Inisialisasi shared preferences

        usernameTextView = view.findViewById(R.id.txt_username)
        emailTextView = view.findViewById(R.id.txt_email)
        noTeleponTextView = view.findViewById(R.id.txt_notlp)
        logoutButton = view.findViewById(R.id.btn_logut)

        // Mengambil data pengguna dari ViewModel (sudah login) dan menampilkannya di profil
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                // Gunakan lifecycleScope untuk menjalankan suspending function
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val user = userViewModel.getUserFromFirestore(email)
                    if (user != null) {
                        // Gunakan data pengguna untuk mengisi tampilan
                        withContext(Dispatchers.Main) {
                            usernameTextView.text = user.username
                            emailTextView.text = user.email
                            noTeleponTextView.text = user.noTelepon
                        }
                    }
                }
            }
        }


        logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah Anda yakin ingin keluar?")
            builder.setPositiveButton("Ya") { _, _ ->
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Logout")
                progressDialog.setMessage("Sedang proses logout...")
                progressDialog.show()

                // Logout dan lakukan operasi terkait logout lainnya

                FirebaseAuth.getInstance().signOut()
                sharedPrefUser.setLoggedIn(false) // Set status login menjadi false

                // Menutup progress dialog
                progressDialog.dismiss()

                // Kembali ke halaman login hanya jika logout berhasil
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // Logout gagal, mungkin menangani kesalahan di sini jika diperlukan
                    Toast.makeText(requireContext(), "Logout gagal", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        return view
    }
    private fun setUpCartViewModel() {
        val viewModelFactory = ViewModelFactory(requireActivity().application)
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }
}
