package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeempat.R
import com.example.challengeempat.adapter.AdapterHome
import com.example.challengeempat.adapter.AdapterKategori
import com.example.challengeempat.databinding.FragmentHomeBinding
import com.example.challengeempat.model.Data
import com.example.challengeempat.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var adapterHome: AdapterHome
    private lateinit var adapterKategori: AdapterKategori
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapterHome = AdapterHome(ArrayList(), true) { item ->
            val bundle = Bundle()
            bundle.putParcelable("item", item)

            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
        binding.rvListMenu.adapter = adapterHome
        binding.rvListMenu.setHasFixedSize(true)

        val recyclerView: RecyclerView = binding.rvKategori
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialisasi adapter dengan daftar kosong
        adapterKategori = AdapterKategori(arrayListOf()) // Gunakan daftar kosong sementara

        recyclerView.adapter = adapterKategori

        homeViewModel.listView.observe(viewLifecycleOwner) { isListView ->
            toggleLayout(isListView)
        }

        observeViewModel()
        initUI()
        return binding.root
    }

    private fun updateRecyclerView(menuItems: List<Data>) {
        adapterHome.updateData(menuItems)
    }

    private fun initUI() {
        binding.btnSwitch.setOnClickListener {
            val currentListViewValue = homeViewModel.listView.value ?: true
            homeViewModel.listView.value = !currentListViewValue
        }
    }

    private fun observeViewModel() {
        homeViewModel.menu.observe(viewLifecycleOwner) { menuItems ->
            updateRecyclerView(menuItems)
        }
    }

    private fun toggleLayout(isListView: Boolean) {
        if (isListView) {
            showLinear()
        } else {
            showGrid()
        }
    }

    private fun showGrid() {
        binding.rvListMenu.layoutManager = GridLayoutManager(requireActivity(), 2)
        adapterHome.isGrid = true
    }

    private fun showLinear() {
        binding.rvListMenu.layoutManager = LinearLayoutManager(requireActivity())
        adapterHome.isGrid = false
    }
}
