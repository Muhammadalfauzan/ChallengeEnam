package com.example.challengeempat.model




data class ApiOrderRequest( val username: String,
                            val total: Int,
                            val orders: List<OrderItem>)
