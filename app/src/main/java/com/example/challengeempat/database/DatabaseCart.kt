package com.example.challengeempat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartData::class], version = 2)
abstract class DatabaseCart : RoomDatabase() {
    abstract fun cartDao(): CartDao

}
