package com.example.challengeempat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.challengeempat.LoginActivity
import com.example.challengeempat.R
import com.example.challengeempat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // Pengguna belum login, arahkan ke halaman login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Tutup `MainActivity` agar pengguna tidak dapat kembali
        } else {
            // Pengguna sudah login, inisialisasi tampilan `MainActivity`
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            val bottomNavView = binding.buttomNav
            NavigationUI.setupWithNavController(bottomNavView, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    fun showButtomNav() {
        binding.buttomNav.visibility = View.VISIBLE
    }

    fun hideButtomNav() {
        binding.buttomNav.visibility = View.GONE
    }
}
