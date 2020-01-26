package com.ade.itemsinmap
import androidx.room.*

class DataBase {
    @Entity(tableName = "coords")
    data class Coor(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "lat") val lat: Double,
        @ColumnInfo(name = "lng") val lng: Double
    )
    @Dao
    interface UserDao {
        @Query("SELECT * FROM coords")
        fun getAll(): List<Coor>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(vararg coor: Coor)

        @Query("DELETE FROM coords WHERE lat IN (:coorLat) AND lng IN (:coorLng)")
        fun delete(coorLat: Double, coorLng: Double)
    }
    @Database(entities = [Coor::class], version = 1, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }

}