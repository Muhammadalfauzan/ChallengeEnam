package com.example.challengeempat.database

import android.os.Parcelable

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "cart")
data class CartData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var imageFood: Int,
    var nameFood: String,
    var hargaPerItem: Int,
    var quantity : Int,
    var note: String?,

) : Parcelable
