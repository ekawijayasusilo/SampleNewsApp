package com.example.samplenewsapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.samplenewsapp.data.category.model.CategoryEntity
import com.example.samplenewsapp.data.category.service.CategoryDao

@Database(entities = [CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}