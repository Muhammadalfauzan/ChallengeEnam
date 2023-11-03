package com.example.challengeempat.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.challengeempat.api.ApiRestaurant
import com.example.challengeempat.database.CartDao
import com.example.challengeempat.database.CartData
import com.example.challengeempat.model.ApiOrderRequest
import com.example.challengeempat.model.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao, private var apiService: ApiRestaurant) {

    private val executorService: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val _orderPlacedLiveData = MutableLiveData<Boolean>()
    val orderPlacedLiveData: LiveData<Boolean>
        get() = _orderPlacedLiveData

    fun getAllCartItems(): LiveData<List<CartData>> {
        return cartDao.getAllItemCart()
    }


    fun updateItemQuantity(item: CartData, newQuantity: Int) {
        item.quantity =newQuantity
        item.totalHarga = item.hargaPerItem * newQuantity
        executorService.execute {
            cartDao.update(item)
        }
    }

    fun deleteCartItem(cartItem: CartData) {
        executorService.execute {
            cartDao.delete(cartItem)
        }
    }

    fun addCartToUpdate(cart: CartData) {
        executorService.execute {
            cartDao.addOrUpdateCartItem(cart)
        }
    }

    fun updateItemNote(item: CartData, newNote: String) {
        executorService.execute {
            item.note = newNote
            cartDao.update(item)
        }
    }

    fun deleteAllItems() {
        executorService.execute {
            cartDao.deleteALlItems()
        }
    }

    fun calculateTotalPrice(): LiveData<Int> {
        return cartDao.getAllItemCart().map { cartItems ->
            var totalPrice = 0
            for (cartItem in cartItems) {
                totalPrice += cartItem.hargaPerItem * cartItem.quantity
            }
            totalPrice
        }
    }

    fun placeOrder(orderRequest: ApiOrderRequest) {
        apiService.order(orderRequest).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    if (orderResponse != null) {
                        _orderPlacedLiveData.postValue(true)
                    }
                } else {
                    _orderPlacedLiveData.postValue(false)
                    Log.e("CartRepository", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                _orderPlacedLiveData.postValue(false)
                Log.e("CartRepository", "Error: ${t.message}")
            }
        })
    }

}
