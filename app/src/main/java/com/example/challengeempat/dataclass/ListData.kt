package com.example.challengeempat.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize data class ListData(val gambar : Int,
                    val namaImg: String,
                    val harga : Int?,
                    val deskripsi : String,
                    var lokasi : String) :Parcelable

