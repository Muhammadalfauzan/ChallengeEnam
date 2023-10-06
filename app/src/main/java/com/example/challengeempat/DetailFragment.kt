package com.example.challengeempat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.challengeempat.databinding.FragmentDetailBinding
import com.example.challengeempat.dataclass.ListData
import com.example.challengeempat.viewmodel.DetailViewModel
import com.example.challengeempat.viewmodel.HomeViewModel

class DetailFragment : Fragment() {

    private var item: ListData? = null
    private  lateinit var binding : FragmentDetailBinding
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var detailViewModel : DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         binding = FragmentDetailBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        setUpDetailViewModel()


        // Terima objek Parcelable dari Bundle
        @Suppress("DEPRECATION")
        item = arguments?.getParcelable("key")


        item?.let { setDataToView( it)
            detailViewModel.setSelectItem(it)
        }
        detailViewModel.vCounter.observe(viewLifecycleOwner){
                count->
            binding.tvQuantity.text =count.toString()
        }

        (activity as MainActivity).hideButtomNav()

        additem()
        lessItem()
        addToCart()
        takeNote()
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showButtomNav()
    }
    private fun setUpDetailViewModel() {
        val viewModelFactory = ViewModelFactory(requireActivity().application)
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
    }
    //  function untuk mengatur data ke tampilan DetailFragment
    private fun setDataToView( item: ListData) {
        binding.imageView.setImageResource(item.gambar)
        binding.tvNama.text = item.namaImg
        binding.tvHargaDetail.text= item.harga.toString()
        binding.tvLokasi.text = item.lokasi
        binding.tvDeskripsi.text = item.deskripsi

        binding.tvLokasi.setOnClickListener {


            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(item.lokasi)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun additem(){
        binding.btnPlus.setOnClickListener {
            detailViewModel.incrementCount()

        }
    }
    private fun lessItem(){
        binding.btnMin.setOnClickListener {
            detailViewModel.decrementCount()

        }
    }

    private fun addToCart(){
        binding.btnAddToCart.setOnClickListener {
            detailViewModel.add()
            Toast.makeText(requireContext(), "Data telah berhasil ditambahkan ke cart", Toast.LENGTH_SHORT).show()
        }
    }
    private fun takeNote() {
        binding.ivDoneNote.setOnClickListener {
            val note = binding.etNote.text.toString()
            detailViewModel.setOrderNote(note)

            binding.etNote.text?.clear()
            Toast.makeText(requireContext(),"Catatan di tambahkan", Toast.LENGTH_SHORT).show()
        }
    }
}



