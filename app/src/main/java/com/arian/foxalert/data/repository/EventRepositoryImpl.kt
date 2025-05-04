package com.arian.foxalert.data.repository

import com.arian.foxalert.data.dao.EventDao
import com.arian.foxalert.data.model.EventEntity
import com.arian.foxalert.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    override suspend fun insertEvent(event: EventEntity) {
        eventDao.insertEvent(event)
    }

    override suspend fun deleteEvent(event: EventEntity) {
        eventDao.deleteEvent(event)
    }

    override fun getAllEvents(): Flow<List<EventEntity>> {
        return eventDao.getAllEvents()
    }

    override fun getEventsByCategory(categoryId: Int): Flow<List<EventEntity>> {
        return eventDao.getEventsByCategory(categoryId)
    }
}
