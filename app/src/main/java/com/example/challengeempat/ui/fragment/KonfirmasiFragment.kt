package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeempat.R
import com.example.challengeempat.ViewModelFactory
import com.example.challengeempat.adapter.CartAdapter
import com.example.challengeempat.databinding.FragmentKonfirmasiBinding
import com.example.challengeempat.managefragment.PaymenDialog
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderItem
import com.example.challengeempat.ui.activity.MainActivity
import com.example.challengeempat.viewmodel.CartViewModel


@Suppress("DEPRECATION")
class KonfirmasiFragment : Fragment() {

    private lateinit var binding: FragmentKonfirmasiBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKonfirmasiBinding.inflate(inflater, container, false)

        setUpCartViewModel()

        cartAdapter = CartAdapter(cartViewModel)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            cartAdapter.updateDataCart(it)
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalHargaPesanan.text = it.toString()
        }

        cartViewModel.orderPlaced.observe(viewLifecycleOwner) {
            if (it) {
                val dialogFragment = PaymenDialog()
                dialogFragment.show(childFragmentManager, "Pesanan berhasil dialaog")
            }
        }

        /* cartViewModel.isLoading.observe(viewLifecycleOwner) {
             showLoading(it)
         }*/
        val mainActivity = requireActivity() as MainActivity
        (activity as MainActivity).hideButtomNav()
        mainActivity.supportActionBar?.hide()


        pesan()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_konfirmasiFragment_to_cartFragment2)
        }
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

    private fun setUpCartViewModel() {
        val viewModelFactory = ViewModelFactory(requireActivity().application)
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }

    private fun pesan() {
        binding.buttonPesanKonfirmasi.setOnClickListener {

            val orderItems = cartViewModel.cartItems.value ?: emptyList()
            if (orderItems.isNotEmpty()) {
                val total = cartViewModel.totalPrice.value ?: 0
                val orderRequest = ApiOrderRequest("Fauzan", total, orderItems.map {
                    OrderItem(it.nameFood, it.quantity, it.note ?: "", it.hargaPerItem)
                })

                cartViewModel.placeOrder(orderRequest)

            } else {
                Toast.makeText(requireContext(), "Keranjang anda kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
