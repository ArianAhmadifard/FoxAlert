package com.arian.foxalert.domain.usecase.event

import com.arian.foxalert.data.model.EventEntity
import com.arian.foxalert.domain.repository.EventRepository
import javax.inject.Inject

class InsertEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(event: EventEntity) = eventRepository.insertEvent(event)
}