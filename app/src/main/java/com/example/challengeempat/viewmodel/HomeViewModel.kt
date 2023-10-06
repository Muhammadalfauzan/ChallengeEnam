package com.example.challengeempat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.dataclass.ListData

class HomeViewModel : ViewModel() {
    val listView = MutableLiveData<Boolean>().apply { value = true
    }
    val menu = MutableLiveData<ArrayList<ListData>>()
}