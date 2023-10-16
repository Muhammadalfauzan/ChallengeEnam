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

    @Update
    fun update(cartData: CartData)

    @Query( "SELECT * FROM cart ORDER BY ID DESC")
    fun getAllItemCart() : LiveData<List<CartData>>

}