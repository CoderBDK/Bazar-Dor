package com.coderbdk.bazardor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.coderbdk.bazardor.data.database.main.ProductCategoryDao
import com.coderbdk.bazardor.data.database.main.ProductCategoryEntity
import com.coderbdk.bazardor.data.database.main.ProductDao
import com.coderbdk.bazardor.data.database.main.ProductEntity

@Database(entities = [ProductCategoryEntity::class, ProductEntity::class],
    version = 1,
)
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
                        "bazardor3_db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}