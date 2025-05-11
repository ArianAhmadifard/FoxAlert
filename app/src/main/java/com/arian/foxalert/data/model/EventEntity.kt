package com.arian.foxalert.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["name"],
        childColumns = ["categoryName"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("categoryName")]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: LocalDate, // requires a type converter
    val categoryName: String
)
