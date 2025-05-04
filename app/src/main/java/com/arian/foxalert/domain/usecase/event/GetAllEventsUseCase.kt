package com.arian.foxalert.domain.usecase.event

import com.arian.foxalert.domain.repository.EventRepository
import javax.inject.Inject

class GetAllEventsUseCase
    @Inject
    constructor(
        private val eventRepository: EventRepository,
    ) {
        operator fun invoke() = eventRepository.getAllEvents()
    }
