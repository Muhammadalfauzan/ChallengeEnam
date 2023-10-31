package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeempat.R
import com.example.challengeempat.adapter.CartAdapter
import com.example.challengeempat.databinding.FragmentCartBinding
import com.example.challengeempat.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        cartAdapter = CartAdapter(cartViewModel)
        binding.rvCart.setHasFixedSize(true)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.ivCartKosong.visibility = View.VISIBLE
                binding.rvCart.visibility = View.GONE
            } else {
                binding.ivCartKosong.visibility = View.GONE
                binding.rvCart.visibility = View.VISIBLE
                cartAdapter.updateDataCart(it)
            }
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.txtTotalharga.text = it.toString()
        }

        orderItem()

        return binding.root
    }



    private fun orderItem() {

        binding.btnPesan.setOnClickListener {
            if (!cartViewModel.cartItems.value.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_cartFragment_to_konfirmasiFragment)
            } else {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

    }


}
