package com.example.challengeempat.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.database.CartDao
import com.example.challengeempat.database.CartData
import com.example.challengeempat.database.DatabaseCart

import com.example.challengeempat.dataclass.ListData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailViewModel(application: Application): ViewModel(){
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val cartDao: CartDao

    private val _vCounter: MutableLiveData<Int> = MutableLiveData(1)
    val vCounter: LiveData<Int> get() = _vCounter

    // LiveData untuk total harga
    private val _totalHarga: MutableLiveData<Int> = MutableLiveData(0)
    val totalHarga: LiveData<Int> get() = _totalHarga

    private val _orderNote = MutableLiveData<String?>()
    private val orderNote: LiveData<String?> = _orderNote


    private val _selectItem = MutableLiveData<ListData>()
    /*val selectItem: LiveData<ListData> = _selectItem*/
    init {
        val db = DatabaseCart.getDataBase(application)
        cartDao = db.cartDao()
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

    fun setSelectItem(item: ListData) {
        _selectItem.value = item
        (_vCounter.value ?: 1)
    }

    fun setOrderNote(note: String?) {
        _orderNote.value = note
    }
     private fun getOrderNote(): String? {
        return orderNote.value
    }

    fun add() {
        val selectItem = _selectItem.value
        selectItem?.let { item ->
            val itemHarga  = item.harga ?: 0
            val quantity = _vCounter.value ?: 1
            val totalHarga = itemHarga/* * quantity*/

            val itemCart = CartData(
                imageFood = item.gambar,
                nameFood = item.namaImg,
                hargaPerItem = totalHarga,
                quantity = quantity,
                note = getOrderNote()
            )

            insertCart(itemCart)
        }
    }
    private fun insertCart(itemCart: CartData) {
        executorService.execute {
            cartDao.insert(itemCart)
        }
    }
}

