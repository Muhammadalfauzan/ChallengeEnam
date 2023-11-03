package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeempat.R
import com.example.challengeempat.adapter.AdapterHome
import com.example.challengeempat.adapter.AdapterKategori
import com.example.challengeempat.databinding.FragmentHomeBinding
import com.example.challengeempat.model.Data
import com.example.challengeempat.model.DataCategory
import com.example.challengeempat.viewmodel.HomeViewModel
import com.example.challengeempat.viewmodelregister.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var adapterHome: AdapterHome
    private lateinit var adapterKategori: AdapterKategori
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.listView.value = homeViewModel.getLayoutPreference()

        adapterHome = AdapterHome(ArrayList(), true) { item ->
            val bundle = Bundle()
            bundle.putParcelable("item", item)
            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
        binding.rvListMenu.adapter = adapterHome
        binding.rvListMenu.setHasFixedSize(true)
        val recyclerView : RecyclerView = binding.rvKategori
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        // Inisialisasi adapter dengan daftar kosong
        adapterKategori = AdapterKategori(arrayListOf()) // Gunakan daftar kosong sementara
        recyclerView.adapter = adapterKategori


        homeViewModel.listView.observe(viewLifecycleOwner) {
            toggleLayout()
        }

        observeViewModel()
        initUI()

        return binding.root
    }

    private fun setUpRecyclerviewMenu(menuItems: List<Data>) {
        adapterHome.updateData(menuItems)
    }
    private fun setUpRecyclerviewCategory (katItem : List<DataCategory>){
        adapterKategori.updateDataKat(katItem)
    }
    private fun initUI() {
        binding.btnSwitch.setOnClickListener {
            toggleLayout()
        }
    }
    private fun observeViewModel() {
        homeViewModel.listView.observe(viewLifecycleOwner) {
            toggleLayout()
        }
        homeViewModel.menu.observe(viewLifecycleOwner) { menuItems ->
            setUpRecyclerviewMenu(menuItems)

        }
        homeViewModel.categories.observe(viewLifecycleOwner){katItem ->
            setUpRecyclerviewCategory(katItem)
        }
    }
    private fun toggleLayout() {
        val toggleImage = binding.btnSwitch
        val currentLayoutValue = homeViewModel.listView.value ?: homeViewModel.getLayoutPreference()
        switchRv(currentLayoutValue)
        toggleImage.setImageResource(if (currentLayoutValue) R.drawable.list else R.drawable.baseline_grid_view_24)

        toggleImage.setOnClickListener {
            val newListViewValue = !currentLayoutValue
            homeViewModel.listView.value = newListViewValue
            homeViewModel.saveLayoutPreference(newListViewValue)
        }
    }
    private fun switchRv(isListView: Boolean) {
        if (isListView) {
            showGrid()
        } else {
            showLinear()
        }
        binding.btnSwitch.setImageResource(if (isListView) R.drawable.list else R.drawable.baseline_grid_view_24)
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

