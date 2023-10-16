package com.example.challengeempat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val image_url: String,
    val nama: String,
    val harga_format: String,
    val harga: Int,
    val detail: String,
    val alamat_resto: String
): Parcelable