package com.arian.foxalert.domain.repository

import com.arian.foxalert.data.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun deleteCategoryByName(name: String)
    suspend fun deleteCategory(category: CategoryEntity)
    fun getAllCategories(): Flow<List<CategoryEntity>>
}