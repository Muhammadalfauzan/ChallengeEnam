package com.example.challengeempat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengeempat.CartRepository
import com.example.challengeempat.api.Api
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(application: Application) : ViewModel() {

    private val repository: CartRepository = CartRepository(application)

    var cartItems: LiveData<List<CartData>> = repository.getAllCartItems()

    var totalPrice: LiveData<Int> = repository.calculateTotalPrice()


    private val _orderPlacedLiveData = MutableLiveData<Boolean>()
    val orderPlaced: LiveData<Boolean>
        get() = _orderPlacedLiveData


    fun updateQuantity(cartItem: CartData, newQuantity: Int) {
        cartItem.quantity = newQuantity
        cartItem.totalHarga = cartItem.hargaPerItem * newQuantity
        repository.updateItemQuantity(cartItem, newQuantity)

    }
    fun deleteCartItem(cartItem: CartData) {
        repository.deleteCartItem(cartItem)

    }

    fun deleteAllItems() {
        repository.deleteAllItems()
    }
    fun placeOrder(orderRequest: ApiOrderRequest) {
        val apiService = Api.apiService

        val call = apiService.order(orderRequest)

        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    if (orderResponse != null) {
                        _orderPlacedLiveData.postValue(true)
                        // Lakukan tindakan setelah pesanan berhasil
                    }
                } else {
                    _orderPlacedLiveData.postValue(false)
                    Log.e("KonfirmasiFragment", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                _orderPlacedLiveData.postValue(false)
                Log.e("KonfirmasiFragment", "Error: ${t.message}")
            }
        })
    }

}