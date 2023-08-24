package com.example.shacklehotelbuddy.di.modules

import android.content.Context
import com.example.shacklehotelbuddy.data.local.AppDatabase
import com.example.shacklehotelbuddy.data.local.dao.SearchQueryDao
import com.example.shacklehotelbuddy.data.local.repository.AbstractLocalRepo
import com.example.shacklehotelbuddy.data.local.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun providesLocalRepository(searchQueryDao: SearchQueryDao): AbstractLocalRepo {
        return LocalRepository(searchQueryDao)
    }

    @Singleton
    @Provides
    fun provideSearchQueryDao(appDatabase: AppDatabase): SearchQueryDao {
        return appDatabase.searchQueryDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}