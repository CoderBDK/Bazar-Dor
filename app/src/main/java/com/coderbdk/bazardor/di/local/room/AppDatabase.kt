package com.coderbdk.bazardor.di.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.coderbdk.bazardor.di.local.room.main.ProductCategoryDao
import com.coderbdk.bazardor.di.local.room.main.ProductCategoryEntity
import com.coderbdk.bazardor.di.local.room.main.ProductDao
import com.coderbdk.bazardor.di.local.room.main.ProductEntity

@Database(entities = [ProductCategoryEntity::class, ProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "bazardor_db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}