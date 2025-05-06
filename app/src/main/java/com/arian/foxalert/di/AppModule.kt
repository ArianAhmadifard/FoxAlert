package com.arian.foxalert.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.arian.foxalert.data.FoxDatabase
import com.arian.foxalert.data.dao.CategoryDao
import com.arian.foxalert.data.dao.EventDao
import com.arian.foxalert.data.model.Converters
import com.arian.foxalert.data.repository.CategoryRepositoryImpl
import com.arian.foxalert.data.repository.EventRepositoryImpl
import com.arian.foxalert.domain.repository.CategoryRepository
import com.arian.foxalert.domain.repository.EventRepository
import com.arian.foxalert.domain.usecase.cateogry.DeleteCategoryUseCase
import com.arian.foxalert.domain.usecase.cateogry.GetAllCategoriesUseCase
import com.arian.foxalert.domain.usecase.cateogry.InsertCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideRoomAppDatabase(
        @ApplicationContext context: Context,
    ): FoxDatabase =
        Room.databaseBuilder(
            context,
            FoxDatabase::class.java,
            "fox_db.db",
        )
            .addTypeConverter(Converters())
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideEventDao(db: FoxDatabase): EventDao = db.eventDao()

    @Provides
    fun provideCategoryDao(db: FoxDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepositoryImpl(eventDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }
}
