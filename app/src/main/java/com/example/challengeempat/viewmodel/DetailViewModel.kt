package com.example.challengeempat.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.CartRepository
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.MenuItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailViewModel(application: Application): ViewModel(){
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val _vCounter: MutableLiveData<Int> = MutableLiveData(1)
    val vCounter: LiveData<Int> = _vCounter

    private val cartRepository: CartRepository
    // LiveData untuk total harga
    private val _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
    val totalHarga: LiveData<Int>  = _totalHarga

    private val _orderNote = MutableLiveData<String?>()
    private val orderNote: LiveData<String?> = _orderNote

    private val _selectItem = MutableLiveData<MenuItem>()

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

    fun setSelectItem(item: MenuItem) {
        _selectItem.value = item
        _totalHarga.value = item.harga
    }

    fun setOrderNote(note: String?) {
        _orderNote.value = note
    }
     private fun getOrderNote(): String? {
        return orderNote.value
    }

    fun addToCart() {
        val selectItem = _selectItem.value

        selectItem?.let {
            val cartItem =
                totalHarga.value?.let { totalHargaValue ->
                vCounter.value?.let { vCounterValue ->
                    CartData(
                        image_url = it.image_url,
                        nameFood = it.nama,
                        hargaPerItem = totalHargaValue,
                        quantity = vCounterValue ,
                        note = getOrderNote(),
                        totalHarga = it.harga
                    )
                }
            }

            cartItem?.let { totalHarga -> insertCart(totalHarga) }
        }
    }


    private fun insertCart(itemCart: CartData) {
            cartRepository.insertData(itemCart)
        }

}

