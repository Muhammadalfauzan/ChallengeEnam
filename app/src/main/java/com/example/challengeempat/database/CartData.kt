package com.example.challengeempat.database

import android.os.Parcelable

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "cart")
data class CartData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageurl: String,
    val nameFood: String,
    val hargaPerItem: Int,
    var quantity: Int,
    var totalHarga : Int,
    var note: String ? =null,


    ) : Parcelable
