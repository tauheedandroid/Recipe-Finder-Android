package com.abasyn.recipefinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteRecipeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
}
