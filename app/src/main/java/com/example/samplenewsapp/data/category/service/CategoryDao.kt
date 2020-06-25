package com.example.samplenewsapp.data.category.service

import androidx.room.Dao
import androidx.room.Query
import com.example.samplenewsapp.data.category.model.CategoryEntity
import io.reactivex.Single

@Dao
interface CategoryDao {
    @Query("select * from category")
    fun getAll(): Single<List<CategoryEntity>>
}