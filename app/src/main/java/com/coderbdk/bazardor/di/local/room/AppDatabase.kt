package com.coderbdk.bazardor.di.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.coderbdk.bazardor.di.local.room.main.ProductCategoryDao
import com.coderbdk.bazardor.di.local.room.main.ProductCategoryEntity

@Database(entities = [ProductCategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productCategoryDao(): ProductCategoryDao

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
        fun obtain(): AppDatabase{
            return INSTANCE!!
        }
    }
}