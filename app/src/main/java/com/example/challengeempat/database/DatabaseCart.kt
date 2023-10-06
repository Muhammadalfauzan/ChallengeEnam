package com.example.challengeempat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CartData::class], version = 1)
abstract class DatabaseCart : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private var INSTANCE: DatabaseCart? = null

        @JvmStatic
        fun getDataBase(context: Context): DatabaseCart {
            if (INSTANCE == null) {
                synchronized(DatabaseCart::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseCart::class.java, "cart_database"
                    )
                        .build()
                }
            }
            return INSTANCE as DatabaseCart
        }


    }
}