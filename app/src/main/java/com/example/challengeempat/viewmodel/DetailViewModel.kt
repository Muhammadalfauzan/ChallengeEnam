package com.example.challengeempat.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.database.CartData
import com.example.challengeempat.modelapi.Data
import com.example.challengeempat.repository.CartRepository

class DetailViewModel(application: Application): ViewModel(){

    private val _vCounter: MutableLiveData<Int> = MutableLiveData(1)
    val vCounter: LiveData<Int> = _vCounter

    private val cartRepository: CartRepository
    // LiveData untuk total harga
    private val _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
    val totalHarga: LiveData<Int>  = _totalHarga



    private val _selectItem = MutableLiveData<Data>()

    init {
        cartRepository = CartRepository(application)
    }

    fun incrementCount() {
        _vCounter.value?.let { currentCount ->
            val newCount = currentCount + 1
            _vCounter.postValue(newCount)

        }
    }

    fun decrementCount() {
        _vCounter.value?.let { currentCount ->
            if (currentCount > 1) {
                val newCount = currentCount - 1
                _vCounter.postValue(newCount)

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
                        image_url = selectedItem.imageUrl,
                        nameFood = selectedItem.nama,
                        hargaPerItem = totalHargaValue,
                        quantity = vCounterValue,
                        note = null,
                        totalHarga = newTotalHarga
                    )

                    cartRepository.addCartToUpdate(newItem)
                }
            }
        }
    }

}

