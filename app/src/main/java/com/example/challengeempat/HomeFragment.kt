package com.example.challengeempat


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeempat.adapter.AdapterHome
import com.example.challengeempat.databinding.FragmentHomeBinding
import com.example.challengeempat.dataclass.ListData
import com.example.challengeempat.sharedpref.ViewPreference
import com.example.challengeempat.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var viewPreferences: ViewPreference
    private lateinit var adapterHome: AdapterHome
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private val listMenu = ArrayList<ListData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewPreferences = ViewPreference(requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        homeViewModel.listView.value = viewPreferences.getLayoutPref()

        adapterHome = AdapterHome(listMenu, homeViewModel.listView.value ?: true)
        binding.rvListMenu.adapter = adapterHome

        binding.rvListMenu.setHasFixedSize(true)
        if (listMenu.isEmpty()) {
            listMenu.addAll(getMenu())
        }
        showRecyclerListVertical()

        homeViewModel.listView.observe(viewLifecycleOwner) {
            toggleLayout()
        }

        homeViewModel.menu.observe(viewLifecycleOwner) { menu ->
            updateRecyclerView(menu)
        }

        observeViewModel()
        initUI()

        return binding.root
    }

    private fun initUI() {

        adapterHome = AdapterHome(listMenu, homeViewModel.listView.value ?: true) { item ->
            val bundle = bundleOf("key" to item)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment2, bundle)
        }
        binding.rvListMenu.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvListMenu.adapter = adapterHome

        binding.btnSwitch.setOnClickListener {
            toggleLayout()
        }

        if (listMenu.isEmpty()) {
            listMenu.addAll(getMenu())
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
        val currentLayoutValue =
            homeViewModel.listView.value ?: viewPreferences.getLayoutPref()

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

        binding.btnSwitch.setImageResource(
            if (isListView) R.drawable.list else R.drawable.baseline_grid_view_24
        )
    }

    private fun showRecyclerListVertical() {
        binding.rvListMenu.layoutManager = GridLayoutManager(requireActivity(), 2)
        adapterHome = AdapterHome(listMenu)
        binding.rvListMenu.adapter = adapterHome
    }

    private fun getMenu(): ArrayList<ListData> {
        val menuResto = ArrayList<ListData>()

        menuResto.add(
            ListData(R.drawable.salmonsusi, "Salmon Sushi", 90000, "Sushi adalah aneka macam bentuk set cuka dan nasi," +
                    " seperti Nigiri Sushi yaitu nasi cuka kepal yang diatasnya diberi ikan, Chirashi sushi yaitu nasi cuka yang disajikan " +
                    "di atas mangkuk lalu diatasnya ditaburi ikan", "Jl. Perintis No.57, Akcaya, Kec. Pontianak Sel., Kota Pontianak, Kalimantan Barat 78115"
            )
        )

        menuResto.add(
            ListData(R.drawable.pizza, "Pizza", 80000,
                "varian dari pangsit tradisional china yang disajikan dengan cara di kukus dan di goreng", "Alamat 3"))
        menuResto.add(
            ListData(
                R.drawable.ayammcd,
                "Ayam",
                harga = 40000,
                "Ayam Spicy\", 30000,\"Ayam Goreng kami adalah hidangan yang sangat populer dan lezat yang pasti akan memanjakan lidah Anda. " +
                        "Kami mengambil potongan ayam berkualitas tinggi, merendamnya dalam bumbu rahasia kami yang kaya rempah-rempah, dan menggorengnya" +
                        " hingga keemasan yang sempurna.",
                "Richeese Factory, Jl. Jend. Sudirman No.46, Kotabaru, Kec. Gondokusuman, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55224"
            )
        )
        menuResto.add(
            ListData(
                R.drawable.minum,
                "Strawberry Milk",
                harga = 25000,
                "Our Strawberry Milk is made with care, starting with the freshest strawberries, which are blended" +
                        " to perfection and then mixed with cold, creamy milk.",
                "Mixue Tugu Jogja, Jl. A.M. Sangaji No.11, Cokrodiningratan, Kec. Jetis, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55231"
            )
        )
        menuResto.add(
            ListData(
                R.drawable.dimsum,
                "Dim Sum",
                harga = 30000,
                "varian dari pangsit tradisional china yang disajikan dengan cara di kukus dan di goreng",
                "6CQ7+W29 Modimsum - Condongcatur, Jl. Prawiro Kuat, Ngringin, Condongcatur, Depok, Sleman Regency, Special Region of Yogyakarta 55281"
            )
        )
        menuResto.sortBy { it.harga }
        return menuResto
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(foodItems: ArrayList<ListData>) {
        listMenu.clear()
        listMenu.addAll(foodItems)
        adapterHome.updateData(foodItems)
        adapterHome.isGrid = homeViewModel.listView.value ?: true
        binding.rvListMenu.adapter?.notifyDataSetChanged()
    }

    private fun showGrid() {
        binding.rvListMenu.layoutManager = GridLayoutManager(requireActivity(), 2)
        adapterHome.isGrid = true
        binding.rvListMenu.adapter = adapterHome
    }

    private fun showLinear() {
        binding.rvListMenu.layoutManager = LinearLayoutManager(requireActivity())
        adapterHome.isGrid = false
        binding.rvListMenu.adapter = adapterHome
    }
}
