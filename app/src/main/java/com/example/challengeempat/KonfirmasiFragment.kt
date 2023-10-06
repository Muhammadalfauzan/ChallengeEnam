package com.example.challengeempat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeempat.adapter.CartAdapter
import com.example.challengeempat.database.CartData
import com.example.challengeempat.databinding.FragmentKonfirmasiBinding
import com.example.challengeempat.viewmodel.CartViewModel


@Suppress("DEPRECATION")
class KonfirmasiFragment : Fragment() {
    private lateinit var binding: FragmentKonfirmasiBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartItems: List<CartData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKonfirmasiBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get data
        val arguments = arguments
        if (arguments != null && arguments.containsKey("cartItems")) {
            cartItems = arguments.getParcelableArrayList("cartItems") ?: emptyList()

            val totalPrice = calculateTotalPrice(cartItems)


            binding.tvTotalHargaPesanan.text = totalPrice.toString()
        }
        val application = requireActivity().application
        val cartViewModel = CartViewModel(application)

        val cartAdapter = CartAdapter(cartItems, cartViewModel,isEditable = false)
        recyclerView.adapter = cartAdapter
        return binding.root
    }

    private fun calculateTotalPrice(cartItems: List<CartData>): Int {
        var totalPrice = 0

        for (cartItem in cartItems) {
            totalPrice += cartItem.hargaPerItem * cartItem.quantity
        }
        Log.d("KonfirmasiFragment", "Cart Items: $totalPrice")
        return totalPrice
    }
}
