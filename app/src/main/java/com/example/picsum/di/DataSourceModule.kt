package com.example.picsum.di

import com.example.picsum.data.PicsumRepository
import com.example.picsum.data.PicsumRepositoryImpl
import com.example.picsum.data.remote.PicsumDataSource
import com.example.picsum.data.remote.PicsumDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindPicsumDataSource(picsumDataSource: PicsumDataSourceImpl): PicsumDataSource

    @Binds
    abstract fun bindPicsumRepository(picsumRepository: PicsumRepositoryImpl): PicsumRepository
}
