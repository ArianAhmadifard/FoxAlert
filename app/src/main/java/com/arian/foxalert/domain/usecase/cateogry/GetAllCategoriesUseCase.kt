package com.arian.foxalert.domain.usecase.cateogry

import com.arian.foxalert.domain.repository.CategoryRepository
import com.arian.foxalert.domain.repository.EventRepository
import javax.inject.Inject

class GetAllCategoriesUseCase
    @Inject
    constructor(
        private val categoryRepository: CategoryRepository,
    ) {
        operator fun invoke() = categoryRepository.getAllCategories()
    }
