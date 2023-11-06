package com.example.challengeempat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.Data
import com.example.challengeempat.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _vCounter: MutableLiveData<Int> = MutableLiveData(1)
    val vCounter: LiveData<Int> = _vCounter

    private val _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
    private val totalHarga: LiveData<Int> = _totalHarga

    private val _selectItem = MutableLiveData<Data>()

    fun incrementCount() {
        _vCounter.value?.let { currentCount ->
            val newCount = currentCount + 1
            _vCounter.value = newCount
        }
    }

    fun decrementCount() {
        _vCounter.value?.let { currentCount ->
            if (currentCount > 1) {
                val newCount = currentCount - 1
                _vCounter.value = newCount
            }
        }
    }

    fun setSelectItem(item: Data) {
        _selectItem.value = item
        _totalHarga.value = item.harga
    }

    fun addToCart() {
        val selectItem = _selectItem.value

        selectItem?.let { selectedItem ->
            totalHarga.value?.let { totalHargaValue ->
                vCounter.value?.let { vCounterValue ->
                    val newTotalHarga = selectedItem.harga * vCounterValue
                    _totalHarga.value = newTotalHarga
                    // Buat objek CartData baru berdasarkan detail item yang dipilih
                    val newItem = CartData(
                        imageurl = selectedItem.imageUrl,
                        nameFood = selectedItem.nama,
                        hargaPerItem = totalHargaValue,
                        quantity = vCounterValue,
                        note = null,
                        totalHarga = newTotalHarga
                    )

                    viewModelScope.launch {
                        cartRepository.addCartToUpdate(newItem)
                    }
                }
            }
        }
    }
}
