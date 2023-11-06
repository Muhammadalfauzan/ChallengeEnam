package com.example.challengeempat.managefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.challengeempat.R
import com.example.challengeempat.databinding.FragmentPaymentSuccesDialogBinding
import com.example.challengeempat.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymenDialog : DialogFragment() {

    private lateinit var binding: FragmentPaymentSuccesDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentSuccesDialogBinding.inflate(layoutInflater, container, false)

        backToHome()
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()

        (activity as MainActivity).showButtomNav()
        val mainActivity = requireActivity() as MainActivity

        // Tampilkan kembali App Bar
        mainActivity.supportActionBar?.show()

        // Tampilkan kembali bottom navigation view jika perlu
        mainActivity.showButtomNav()
    }

    private fun backToHome() {
        binding.btnBackHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

}