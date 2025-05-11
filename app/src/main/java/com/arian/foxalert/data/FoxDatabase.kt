package com.arian.foxalert.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arian.foxalert.data.dao.CategoryDao
import com.arian.foxalert.data.dao.EventDao
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.data.model.Converters
import com.arian.foxalert.data.model.EventEntity

@Database(entities = [EventEntity::class, CategoryEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoxDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun categoryDao(): CategoryDao
}
