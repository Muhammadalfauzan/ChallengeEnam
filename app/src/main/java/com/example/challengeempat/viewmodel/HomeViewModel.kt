package com.example.challengeempat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.model.Data
import com.example.challengeempat.model.MenuItem

class HomeViewModel : ViewModel() {
    val listView = MutableLiveData<Boolean>().apply { value = true
    }
    val menu = MutableLiveData<ArrayList<MenuItem>>()
    val menuKat = MutableLiveData<ArrayList<Data>>()
}