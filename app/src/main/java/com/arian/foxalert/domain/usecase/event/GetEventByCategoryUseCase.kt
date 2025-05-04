package com.arian.foxalert.domain.usecase.event

import com.arian.foxalert.domain.repository.EventRepository
import javax.inject.Inject

class GetEventByCategoryUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(categoryId: Int) = eventRepository.getEventsByCategory(categoryId)
}