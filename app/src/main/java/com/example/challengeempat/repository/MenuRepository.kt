package com.example.challengeempat.repository

import com.example.challengeempat.api.ApiRestaurant
import javax.inject.Inject

class MenuRepository
    @Inject
    constructor(private var apiService: ApiRestaurant){

        suspend fun getAllMenu() = apiService.getAllMenu()

        suspend fun getAllcategory() = apiService.getAllCategory()
}