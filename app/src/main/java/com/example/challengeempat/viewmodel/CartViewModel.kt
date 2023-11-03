package com.example.challengeempat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    val cartItems: LiveData<List<CartData>> = repository.getAllCartItems()
    val totalPrice: LiveData<Int> = repository.calculateTotalPrice()
    val orderPlaced: LiveData<Boolean> = repository.orderPlacedLiveData

    fun updateQuantity(item: CartData, newQuantity: Int) {

        repository.updateItemQuantity(item, newQuantity) }
    fun deleteCartItem(cartItem: CartData) {
        repository.deleteCartItem(cartItem) }

    fun updateNote(cartItem: CartData, newNote: String) {
        repository.updateItemNote(cartItem, newNote) }
    fun deleteAllItems() {
        repository.deleteAllItems() }
    fun placeOrder(orderRequest: ApiOrderRequest) {
        repository.placeOrder(orderRequest) }
}

