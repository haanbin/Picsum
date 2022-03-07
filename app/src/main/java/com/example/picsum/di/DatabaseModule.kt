package com.example.picsum.di

import android.content.Context
import androidx.room.Room
import com.example.picsum.data.local.db.PicsumDataBase
import com.example.picsum.data.local.db.dao.ImageDao
import com.example.picsum.data.local.db.dao.ImageRemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private const val DB_NAME = "Picsum"

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): PicsumDataBase =
        Room.databaseBuilder(
            appContext,
            PicsumDataBase::class.java,
            DB_NAME
        ).build()

    @Provides
    fun provideImageDao(dataBase: PicsumDataBase): ImageDao = dataBase.imageDao()

    @Provides
    fun provideImageRemoteKeysDao(dataBase: PicsumDataBase): ImageRemoteKeysDao =
        dataBase.imageRemoteKeysDao()
}
