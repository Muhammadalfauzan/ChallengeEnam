package com.example.challengeempat.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.api.ApiRestaurant
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

class CartViewModel @Inject constructor(
    private val apiRestaurant: ApiRestaurant,
    private val repository: CartRepository
) : ViewModel() {

    val cartItems: LiveData<List<CartData>> = repository.getAllCartItems()
    val totalPrice: LiveData<Int> = repository.calculateTotalPrice()
    val orderPlaced: LiveData<Boolean> = repository.orderPlacedLiveData

    // Fungsi untuk memperbarui jumlah item di keranjang
    fun updateQuantity(item: CartData, newQuantity: Int) {
        repository.updateItemQuantity(item, newQuantity)
    }

    // Fungsi untuk menghapus item dari keranjang
    fun deleteCartItem(cartItem: CartData) {
        repository.deleteCartItem(cartItem)
    }

    // Fungsi untuk menambahkan atau memperbarui item di keranjang
    fun addOrUpdateCartItem(cart: CartData) {
        repository.addCartToUpdate(cart)
    }

    // Fungsi untuk memperbarui catatan item di keranjang
    fun updateNote(cartItem: CartData, newNote: String) {
        repository.updateItemNote(cartItem, newNote)
    }

    // Fungsi untuk menghapus semua item dari keranjang
    fun deleteAllItems() {
        repository.deleteAllItems()
    }

    // Fungsi untuk menempatkan pesanan
    fun placeOrder(orderRequest: ApiOrderRequest) {
        repository.placeOrder(orderRequest)
    }
}

