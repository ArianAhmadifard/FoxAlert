package com.arian.foxalert.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.data.model.EventEntity
import com.arian.foxalert.domain.usecase.event.DeleteEventUseCase
import com.arian.foxalert.domain.usecase.event.GetAllEventsUseCase
import com.arian.foxalert.domain.usecase.event.InsertEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val deleteEventUseCase: DeleteEventUseCase,
    val insertEventUseCase: InsertEventUseCase,
    val getAllEventsUseCase: GetAllEventsUseCase
) : ViewModel() {

    private val _allEvents: MutableStateFlow<List<EventEntity>> = MutableStateFlow(emptyList())
    val allEvent = _allEvents.asStateFlow()

    init {
        viewModelScope.launch {
            getAllEventsUseCase().collect { events ->
                _allEvents.update {
                    events
                }
            }
        }
    }


    fun deleteEventIfExists(event: EventEntity) {
        viewModelScope.launch {
            val targetCategory =
                _allEvents.value?.find { it.title == event.title && it.description == event.description }
            targetCategory?.let {
                deleteEventUseCase(it)
            }
        }
    }
}