package com.arian.foxalert.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.data.model.EventEntity
import com.arian.foxalert.domain.usecase.cateogry.GetAllCategoriesUseCase
import com.arian.foxalert.domain.usecase.event.DeleteEventUseCase
import com.arian.foxalert.domain.usecase.event.GetAllEventsUseCase
import com.arian.foxalert.domain.usecase.event.GetEventByCategoryUseCase
import com.arian.foxalert.domain.usecase.event.InsertEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val insertEventUseCase: InsertEventUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private var hasInitializedData = false

    private val _categories: MutableStateFlow<List<CategoryEntity>> = MutableStateFlow(emptyList())
    val categories = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { categoryList ->
                _categories.update {
                    categoryList
                }
            }
        }
    }

    fun insertEvent(event: EventEntity) {
        viewModelScope.launch {
            insertEventUseCase(event)
        }
    }

}