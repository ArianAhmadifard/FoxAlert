package com.arian.foxalert.domain.usecase.cateogry

import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.domain.repository.CategoryRepository
import javax.inject.Inject



class InsertCategoryUseCase
@Inject
constructor(
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(category: CategoryEntity) =
        categoryRepository.insertCategory(category)
}
