package com.example.challengeempat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface CartDao {
    @Insert
    fun insert (cartData: CartData)

    @Delete
    fun delete(cartData: CartData)
    @Query("DELETE FROM cart")
    fun deleteALlItems()
    @Update
    fun update(cartData: CartData)

    @Query( "SELECT * FROM cart ORDER BY ID DESC")
    fun getAllItemCart() : LiveData<List<CartData>>


  @Query("Select * FROM cart WHERE nameFood = :name")
  fun getCartItemByName(name : String): CartData?

    fun addOrUpdateCartItem(cartData: CartData) {
        val existingItem = getCartItemByName(cartData.nameFood)
        if (existingItem != null) {
            val newQuantity = existingItem.quantity + cartData.quantity
            existingItem.quantity = newQuantity
            update(existingItem)
        } else {

            insert(cartData)
        }
    }

}
