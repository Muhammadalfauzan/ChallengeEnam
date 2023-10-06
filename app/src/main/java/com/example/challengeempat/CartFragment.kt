package com.example.challengeempat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.challengeempat.adapter.CartAdapter
import com.example.challengeempat.database.CartData
import com.example.challengeempat.databinding.FragmentCartBinding

import com.example.challengeempat.viewmodel.CartViewModel


class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater, container, false)
        val recyclerView: RecyclerView = binding.rvCart
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        cartAdapter = CartAdapter(mutableListOf(), viewModel) // Adapter awal dengan data kosong
        recyclerView.adapter = cartAdapter

        val initialCartItems = viewModel.cartItems.value
        val initialTotalHarga = initialCartItems?.let { calculateTotalPrice(it) } ?: 0

        binding.txtTotalharga.text = "Rp. $initialTotalHarga"

        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.updateDataCart(cartItems)

            val totalHarga = calculateTotalPrice(cartItems)

            binding.txtTotalharga.text = "Rp. $totalHarga"

            if (cartItems.isEmpty()) {
                viewModel.resetTotalPrice()
            }

        }
        binding.btnPesan.setOnClickListener {
            val cartItems = viewModel.cartItems.value
            if (cartItems != null && cartItems.isNotEmpty()) {

                val bundle = Bundle()
                bundle.putParcelableArrayList("cartItems", ArrayList(cartItems))
                findNavController().navigate(R.id.action_cartFragment_to_konfirmasiFragment, bundle)


                viewModel.clearCartItems()
            } else {

                Toast.makeText(requireContext(), "Keranjang kosong", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun calculateTotalPrice(cartItems: List<CartData>): Int {
        var totalPrice = 0

        for (cartItem in cartItems) {
            totalPrice += cartItem.hargaPerItem * cartItem.quantity
        }

        return totalPrice
    }
}

