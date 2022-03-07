package com.example.picsum.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.picsum.data.local.db.dao.ImageDao
import com.example.picsum.data.local.db.dao.ImageRemoteKeysDao
import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.local.db.entity.ImageRemoteKeysEntity

@Database(
    entities = [ImageEntity::class, ImageRemoteKeysEntity::class],
    version = 1
)
abstract class PicsumDataBase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    abstract fun imageRemoteKeysDao(): ImageRemoteKeysDao
}