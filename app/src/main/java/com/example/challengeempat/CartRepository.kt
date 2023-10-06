package com.example.challengeempat

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.challengeempat.database.CartDao
import com.example.challengeempat.database.CartData
import com.example.challengeempat.database.DatabaseCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CartRepository(application: Application) {

    private val cartDao: CartDao
    private val executorService : ExecutorService = Executors.newSingleThreadScheduledExecutor()
    init {
        val database = DatabaseCart.getDataBase(application)
        cartDao = database.cartDao()
    }

    fun getAllCartItems(): LiveData<List<CartData>> {
        return cartDao.getAllItemCart()
    }

    fun updateItemQuantity(item: CartData, newQuantity: Int) {
        item.quantity = newQuantity
        executorService.execute { cartDao.update(item) }
    }
    fun deleteCartItem(cartItem: CartData) {
        executorService.execute {
            cartDao.delete(cartItem)
        }
    }
    suspend fun clearAllCartItems() {
        withContext(Dispatchers.IO) {
        }
    }
}