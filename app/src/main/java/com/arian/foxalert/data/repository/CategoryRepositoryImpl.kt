package com.arian.foxalert.data.repository

import com.arian.foxalert.data.dao.CategoryDao
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    override suspend fun deleteCategoryByName(name: String) {
        categoryDao.deleteCategoryByName(name)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

}
