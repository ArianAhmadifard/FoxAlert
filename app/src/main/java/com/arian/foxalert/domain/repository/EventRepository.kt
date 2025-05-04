package com.arian.foxalert.domain.repository

import com.arian.foxalert.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun insertEvent(event: EventEntity)
    suspend fun deleteEvent(event: EventEntity)
    fun getAllEvents(): Flow<List<EventEntity>>
    fun getEventsByCategory(categoryId: Int): Flow<List<EventEntity>>
}