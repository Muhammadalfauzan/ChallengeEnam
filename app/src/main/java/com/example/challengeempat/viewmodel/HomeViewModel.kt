package com.example.challengeempat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengeempat.model.Data
import com.example.challengeempat.model.DataCategory
import com.example.challengeempat.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val menuRepository: MenuRepository) : ViewModel() {
    val listView = MutableLiveData<Boolean>().apply { value = true }
    val menu = MutableLiveData<List<Data>>()

    private val _categories = MutableLiveData<List<DataCategory>>()
    val categories: LiveData<List<DataCategory>> = _categories
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        fetchMenu()
        fetchCategories()
    }

    private fun fetchMenu() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = menuRepository.getAllMenu()
                withContext(Dispatchers.Main) {
                    _loading.postValue(false)
                }
                if (response.code() == 200) {
                    val apiMenuResponse = response.body()
                    if (apiMenuResponse != null) {
                        val menuData = apiMenuResponse.datamenu
                        menu.postValue(menuData)
                        Log.d("HomeFragment", "Menu data loaded successfully: ${menuData.size} items")
                    } else {
                        Log.e("HomeFragment", "Menu data body is null")
                    }
                } else {
                    Log.e("HomeFragment", "Menu API request failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.postValue(false)
                }
                Log.e("HomeFragment", "Menu API request failed: ${e.message}")
            }
        }
    }

    private fun fetchCategories() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = menuRepository.getAllcategory()
                withContext(Dispatchers.Main) {
                    _loading.postValue(false)
                }
                if (response.isSuccessful) {
                    val apiCategoriesResponse = response.body()
                    if (apiCategoriesResponse != null) {
                        val categories = apiCategoriesResponse.data
                        _categories.postValue(categories)
                        Log.d("HomeFragment", "Categories loaded successfully: ${categories.size} items")
                    } else {
                        Log.e("HomeFragment", "Categories data body is null")
                    }
                } else {
                    Log.e("HomeFragment", "Categories API request failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.postValue(false)
                }
                Log.e("HomeFragment", "Categories API request failed: ${e.message}")
            }
        }
    }
}
