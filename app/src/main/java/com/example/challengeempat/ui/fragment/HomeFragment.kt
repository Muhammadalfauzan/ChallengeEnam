package com.example.challengeempat.ui.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.challengeempat.api.Api
import com.example.challengeempat.databinding.FragmentHomeBinding
import com.example.challengeempat.model.ApiKategori
import com.example.challengeempat.model.ApiMenuResponse
import com.example.challengeempat.model.MenuItem
import com.example.challengeempat.sharedpref.ViewPreference
import com.example.challengeempat.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var viewPreferences: ViewPreference
    private lateinit var adapterHome: AdapterHome
    private lateinit var adapterKategori: AdapterKategori
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewPreferences = ViewPreference(requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        homeViewModel.listView.value = viewPreferences.getLayoutPref()

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
        fetchAllData()
        fetchAllKategori()
        return binding.root
    }


    private fun updateRecyclerView(menuItems: List<MenuItem>) {
        adapterHome.updateData(menuItems)

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
            updateRecyclerView(menuItems)

        }
    }

    private fun toggleLayout() {
        val toggleImage = binding.btnSwitch
        val currentLayoutValue = homeViewModel.listView.value ?: viewPreferences.getLayoutPref()
        switchRv(currentLayoutValue)

        toggleImage.setImageResource(if (currentLayoutValue) R.drawable.list else R.drawable.baseline_grid_view_24)

        toggleImage.setOnClickListener {
            val newListViewValue = !currentLayoutValue
            homeViewModel.listView.value = newListViewValue
            viewPreferences.saveLayoutPref(newListViewValue)
        }
    }

    private fun switchRv(isListView: Boolean) {
        if (isListView) {
            showLinear()
        } else {
            showGrid()

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

    private fun fetchAllData() {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<ApiMenuResponse> = Api.apiService.getAllMenu()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiMenuResponse = response.body()
                        if (apiMenuResponse != null) {
                            val menuItems = apiMenuResponse.data

                            showLoading(false)
                            adapterHome.updateData(menuItems)

                            Log.d("HomeFragment", "Data loaded successfully: ${menuItems.size} items")
                        } else {
                            Log.e("HomeFragment", "Data body is null")
                        }
                    } else {
                        Log.e("HomeFragment", "API request failed with code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Log.e("HomeFragment", "API request failed: ${e.message}")
                }
            }
        }
    }

    private fun fetchAllKategori() {
        Api.apiService.getKategori().enqueue(object : Callback<ApiKategori> {
            override fun onResponse(call: Call<ApiKategori>, response: Response<ApiKategori>) {
                if (response.isSuccessful) {
                    val apiMenuResponse = response.body()
                    if (apiMenuResponse != null) {
                        val menuKat = apiMenuResponse.dataKat
                        showLoading(false)
                        adapterKategori.updateDataKat(menuKat)
                        Log.d("HomeFragment", "Data loaded successfully: ${menuKat.size} items")
                    } else {
                        Log.e("HomeFragment", "Data body is null")
                    }
                } else {
                    Log.e("HomeFragment", "API request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiKategori>, t: Throwable) {
                showLoading(false)
                Log.e("HomeFragment", "API request failed: ${t.message}")
            }
        })
    }


    private fun showLoading(state: Boolean) {
        if (state) {

            binding.pgLoading.visibility = View.VISIBLE
            binding.rvListMenu.visibility = View.GONE
        } else
            binding.pgLoading.visibility = View.GONE
        binding.rvListMenu.visibility = View.VISIBLE
    }
}
