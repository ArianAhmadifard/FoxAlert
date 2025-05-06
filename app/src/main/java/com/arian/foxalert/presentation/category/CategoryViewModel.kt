package com.arian.foxalert.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.domain.usecase.cateogry.DeleteCategoryUseCase
import com.arian.foxalert.domain.usecase.cateogry.GetAllCategoriesUseCase
import com.arian.foxalert.domain.usecase.cateogry.InsertCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    val insertCategoryUseCase: InsertCategoryUseCase,
    val deleteCategoryUseCase: DeleteCategoryUseCase,
    val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private val _categories: MutableStateFlow<List<CategoryEntity>?> =
        MutableStateFlow(null)
    val categories = _categories.asStateFlow()

    private var hasInitializedData = false

    init {
        observeCategories()
        checkAndPopulateInitialData()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase()
                .collect { list ->
                    _categories.update {
                        list
                    }
                }
        }
    }

    private fun checkAndPopulateInitialData() {
        viewModelScope.launch {
            val initialList = getAllCategoriesUseCase().first()

            if (initialList.isEmpty() && !hasInitializedData) {
                val defaultCategories = listOf(
                    CategoryEntity(name = BIRTH_DAY, color = "#FF5722"),
                    CategoryEntity(name = MEETING, color = "#FF0000"),
                )
                defaultCategories.forEach { category ->
                    insertCategoryUseCase(category)
                }
                hasInitializedData = true
            }
        }
    }

    fun insertCategoryIfNotExists(category: CategoryEntity) {
        viewModelScope.launch {
            val targetCategory = _categories.value?.find { it.name == category.name }
            if(targetCategory == null) {
                insertCategoryUseCase(category)
            }
        }
    }

    fun deleteCategoryIfExists(category: CategoryEntity) {
        viewModelScope.launch {
            val targetCategory = _categories.value?.find { it.name == category.name}
            targetCategory?.let {
                deleteCategoryUseCase(it)
            }
        }
    }

    companion object {
        const val BIRTH_DAY = "BirthDay"
        const val MEETING = "Meeting"
    }
}