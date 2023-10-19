package com.example.challengeempat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CartData::class], version = 2)
abstract class DatabaseCart : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private var INSTANCE: DatabaseCart? = null

        @JvmStatic
        fun getInstance(context: Context): DatabaseCart {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseCart::class.java,
                    "cart"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}
