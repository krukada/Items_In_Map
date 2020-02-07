package com.ade.itemsinmap.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Items::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ItemsDao
}