package com.ade.itemsinmap.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<Items>

    @Query("SELECT * FROM items WHERE uid = (:id)")
    fun getAllbyId(id: Int): Items

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: Items)

    @Query("UPDATE items set visible = (:visible) WHERE service = (:service)")
    fun updateVisiblebyService(service: String, visible: Boolean)
}