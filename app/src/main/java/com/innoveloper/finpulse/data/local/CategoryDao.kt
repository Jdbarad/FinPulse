package com.innoveloper.finpulse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>
}