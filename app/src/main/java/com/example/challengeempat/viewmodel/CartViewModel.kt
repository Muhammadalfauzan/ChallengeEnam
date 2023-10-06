package com.example.challengeempat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengeempat.CartRepository
import com.example.challengeempat.database.CartData
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CartRepository = CartRepository(application)
    val cartItems: LiveData<List<CartData>> = repository.getAllCartItems()
    private val _totalPriceLiveData = MutableLiveData<Int>(0)
    val totalPriceLiveData: LiveData<Int>
        get() = _totalPriceLiveData

    // Fungsi untuk menghitung total harga
    private fun calculateTotalPrice(cartItemsList: List<CartData>): Int {
        var totalPrice = 0

        for (cartItem in cartItemsList) {
            // Mengalikan quantity dengan harga per item dan menambahkannya ke total harga
            totalPrice += cartItem.quantity * cartItem.hargaPerItem
        }

        return totalPrice
    }

    // Fungsi untuk mengupdate quantity
    fun updateQuantity(cartItem: CartData, newQuantity: Int) {
        // Update quantity pada cartItem
        cartItem.quantity = newQuantity
        // Update cartItem di repository jika diperlukan
        repository.updateItemQuantity(cartItem, newQuantity)
        // Update total harga
        val currentCartItems = cartItems.value ?: emptyList()
        val totalPrice = calculateTotalPrice(currentCartItems)
        _totalPriceLiveData.value = totalPrice
    }

    fun deleteCartItem(cartItem: CartData) {
        // Hapus cartItem dari repository
        repository.deleteCartItem(cartItem)
        // Update total harga
        val currentCartItems = cartItems.value ?: emptyList()
        val totalPrice = calculateTotalPrice(currentCartItems)
        _totalPriceLiveData.value = totalPrice
    }
    fun resetTotalPrice() {
        _totalPriceLiveData.value = 0

    }
    fun clearCartItems() = viewModelScope.launch {
        repository.clearAllCartItems()
    }
}

