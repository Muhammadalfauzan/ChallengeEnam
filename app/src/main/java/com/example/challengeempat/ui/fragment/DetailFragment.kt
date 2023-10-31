package com.example.challengeempat.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.challengeempat.R
import com.example.challengeempat.databinding.FragmentDetailBinding
import com.example.challengeempat.model.Data
import com.example.challengeempat.ui.activity.MainActivity
import com.example.challengeempat.viewmodel.DetailViewModel
import com.example.challengeempat.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var item: Data? = null
    private lateinit var binding: FragmentDetailBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]


        detailViewModel.vCounter.observe(viewLifecycleOwner) { count ->
            binding.tvQuantity.text = count.toString()
        }

        (activity as MainActivity).hideButtomNav()

        additem()
        lessItem()
        addToCart()
        setDataToView()
        setMaps()

        val mainActivity = requireActivity() as MainActivity

        // Sembunyikan App Bar
        mainActivity.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_detailFragment2_to_homeFragment)
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


    private fun setDataToView() {

        item = arguments?.getParcelable("item")
        item?.let {

            binding.tvNama.text = item?.nama
            binding.tvHargaDetail.text = item?.hargaFormat
            binding.tvDeskripsi.text = item?.detail
            binding.tvLokasi.text = item?.alamatResto

            Glide.with(binding.root.context).load(it.imageUrl).into(binding.ivDetailMenu)

            detailViewModel.setSelectItem(it)
        }

    }

    private fun setMaps() {

        val item = arguments?.getParcelable<Data>("item")

        binding.tvLokasi.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(item?.alamatResto)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun additem() {
        binding.btnPlus.setOnClickListener {
            detailViewModel.incrementCount()

        }
    }

    private fun lessItem() {
        binding.btnMin.setOnClickListener {
            detailViewModel.decrementCount()

        }
    }

    private fun addToCart() {
        binding.btnAddToCart.setOnClickListener {
            detailViewModel.addToCart()
            Toast.makeText(
                requireContext(), "Data telah berhasil ditambahkan ke cart", Toast.LENGTH_SHORT
            ).show()
        }
    }

}
